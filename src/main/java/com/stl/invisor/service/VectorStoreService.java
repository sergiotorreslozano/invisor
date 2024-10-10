package com.stl.invisor.service;

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
import java.util.Arrays;
import java.util.List;

@Service
@ConfigurationProperties
public class VectorStoreService {

    Logger logger = LoggerFactory.getLogger(VectorStoreService.class);

    private SimpleVectorStore simpleVectorStore;

    @Value("${app.tepResources:-classpath:tep/*}")
    private Resource[] tepResources;

    @Value("${app.paypalResources:classpath:paypal/*}")
    private Resource[] paypalResources;

    @Value("${app.vectorstore:/tmp/vectorstore.json}")
    private String vectorStorePath;

    @Autowired
    private EmbeddingClient embeddingClient;

    @PostConstruct
    public void init() throws IOException {
        simpleVectorStore = new SimpleVectorStore(embeddingClient);
        File vectorStoreFile = new File (vectorStorePath);
        if (vectorStoreFile.exists()){
            logger.info("vectorStoreFile exists, reusing existing " + vectorStoreFile.getAbsolutePath());
            simpleVectorStore.load(vectorStoreFile);
        }else {

            Arrays.stream(tepResources)
                    .forEach((res -> {
                        simpleVectorStore.add(generateSplitDocuments(res));
                    }
                    ));
            Arrays.stream(paypalResources)
                    .forEach((res -> {
                        simpleVectorStore.add(generateSplitDocuments(res));
                    }
                    ));
            simpleVectorStore.save(vectorStoreFile);
        }
    }



    private List<Document> generateSplitDocuments(Resource resource)  {
        try {
            logger.info("Spliting documemnts from resource " + resource.getURI());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TikaDocumentReader documentReader = new TikaDocumentReader(resource);
        List<Document> documents = documentReader.get();
        TextSplitter textSplitter = new TokenTextSplitter();
        List<Document> splitDocuments = textSplitter.apply(documents);
        return splitDocuments;
    }

    public List<Document> similaritySearch(SearchRequest search){
        return simpleVectorStore.similaritySearch(search);
    }
}
