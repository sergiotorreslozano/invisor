package com.stl.invisor.service;

import com.stl.invisor.entities.DataDocument;
import com.stl.invisor.entities.DataDocumentPart;
import com.stl.invisor.repository.DataDocumentPartRepository;
import com.stl.invisor.repository.DataDocumentRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@ConfigurationProperties
public class VectorStoreService {

    Logger logger = LoggerFactory.getLogger(VectorStoreService.class);

    private SimpleVectorStore simpleVectorStore;

    private static final UUID uuid = UUID.fromString("01927a27-ef2e-7cc1-9ac3-641b9f0bd38b");
    @Value("${app.documentRoot:-/tmp/data/documentation}")
    private String documentRootPath;

    @Value("${app.vectorstore:-/tmp/data/vectorstore.json}")
    private String vectorStorePath;

    private File vectorStoreFile;

    @Autowired
    private EmbeddingClient embeddingClient;

    @Autowired
    private DataDocumentRepository dataDocumentRepository;

    @Autowired
    private DataDocumentPartRepository dataDocumentPartRepository;

    @PostConstruct
    public void init() throws IOException {
        simpleVectorStore = new SimpleVectorStore(embeddingClient);
        vectorStoreFile = new File(vectorStorePath);
        if (vectorStoreFile.exists()) {
            logger.info("Vector store file exists, reusing: " + vectorStoreFile.getAbsolutePath());
            simpleVectorStore.load(vectorStoreFile);
        } else {
            logger.info("Creating new Vector store");
            // Get all files from the directory structure dynamically
            List<File> allFiles = getAllFilesFromDirectory(documentRootPath);
            logger.info("Found " + allFiles.size() + " files to process.");

            for (File file : allFiles) {
                List<Document> documents = generateSplitDocuments(file);
                List<DataDocument> dataDocuments = dataDocumentRepository.findAll();

                saveDocumentsToRepository(file.getName(), findLastDirectoryFromPath(file), documents);
                simpleVectorStore.add(documents);
            }

            // Save vector store to the specified file
            simpleVectorStore.save(vectorStoreFile);
        }
    }

    /**
     * Get all files from the given directory and its subdirectories.
     */
    private List<File> getAllFilesFromDirectory(String directoryPath) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {
            return paths.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Generate split documents from a file.
     */
    private List<Document> generateSplitDocuments(File file) {
        try {
            logger.info("Splitting documents from file: " + file.getAbsolutePath());
            Resource resource = new org.springframework.core.io.FileSystemResource(file);
            TikaDocumentReader documentReader = new TikaDocumentReader(resource);
            List<Document> documents = documentReader.get();

            TextSplitter textSplitter = new TokenTextSplitter();
            return textSplitter.apply(documents);
        } catch (Exception e) {
            throw new RuntimeException("Error processing file: " + file.getAbsolutePath(), e);
        }
    }

    /**
     * Save documents to the repository.
     */
    private void saveDocumentsToRepository(String filename, String folder, List<Document> documents) {
        List<DataDocumentPart> parts = new ArrayList<>();

        // Create the DataDocument first
        DataDocument dataDocument = new DataDocument.Builder()
                .name(filename)
                .folder(folder)
                .createdBy(uuid)
                .createdTime(Instant.now())
                .updatedBy(uuid)
                .updateTime(Instant.now())
                .build();
        dataDocumentRepository.save(dataDocument);

        for(Document document: documents){
            DataDocumentPart part = new DataDocumentPart.Builder()
                    .id(UUID.fromString(document.getId()))
                    .dataDocument(dataDocument)  // Set the parent DataDocument
                    .createdBy(uuid)
                    .createdTime(Instant.now())
                    .updatedBy(uuid)
                    .updateTime(Instant.now())
                    .build();
            parts.add(part);
        }
        dataDocumentPartRepository.saveAll(parts);

    }

    /**
     * Extracts the last directory from the file's absolute path.
     *
     * @param file The file object.
     * @return The name of the last directory in the file's path.
     */
    private String findLastDirectoryFromPath(File file) {
        // Get the file's parent directory as a Path object
        Path filePath = Paths.get(file.getAbsolutePath());

        // Get the parent directory
        Path parentPath = filePath.getParent();

        if (parentPath != null) {
            // Get the last part of the parent directory (the last folder name)
            return parentPath.getFileName().toString();
        }

        // If the file has no parent, return a default value
        return "/tmp/data/documentation";
    }

    /**
     * Perform a similarity search on the vector store.
     */
    public List<Document> similaritySearch(SearchRequest search) {
        return simpleVectorStore.similaritySearch(search);
    }

    /**
     * Reloads all documents in a given folder.
     * The process first removes the documents from the DB and then triggers a document load.
     * @param folder
     * @throws IOException
     */
    public void reloadDocuments(String folder) throws IOException {
        List<DataDocument> dataDocuments = dataDocumentRepository.findDataDocumentsByFolderWithParts(folder);
        dataDocuments.stream().forEach(dataDocument -> removeFromVectorStore(dataDocument));
        loadFolderInVectorStore(folder);
    }

    private void removeFromVectorStore(DataDocument dataDocument) {

        List<String> ids = dataDocument.getDataDocumentParts().stream()
                .map(dataDocumentPart -> dataDocumentPart.getId().toString())  // Map each UUID to its String representation
                .collect(Collectors.toList());
        simpleVectorStore.delete(ids);
        dataDocumentRepository.delete(dataDocument);
    }

    public void loadFolderInVectorStore(String folder) throws IOException {
        // Get all files from the directory structure dynamically
        List<File> allFiles = getAllFilesFromDirectory(documentRootPath + "/" + folder);
        logger.info("Found " + allFiles.size() + " files to process.");

        for (File file : allFiles) {
            List<Document> documents = generateSplitDocuments(file);
            List<DataDocument> dataDocuments = dataDocumentRepository.findAll();

            saveDocumentsToRepository(file.getName(), folder, documents);
            simpleVectorStore.add(documents);
        }

        // Save vector store to the specified file
        simpleVectorStore.save(vectorStoreFile);
    }

}
