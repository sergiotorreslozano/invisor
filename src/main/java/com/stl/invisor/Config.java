package com.stl.invisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.openai.OpenAiEmbeddingClient;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Configuration
public class Config {

    Logger logger = LoggerFactory.getLogger(Config.class);

    @Value("${app.vectorstore.path:/tmp/vectorstore.json}")
    private String vectorStorePath;

    @Value("${app.documentResource}")
    private String documentResource;

    @Value("${spring.ai.openai.api-key}")
    private String OPENAI_API_KEY;

    @Value("${app.documentResource}")
    private Resource resource;

    @Value("${app.documentResourceEstimates}")
    private Resource resourceEstimates;

    @Value("${app.documentResourceFinancials}")
    private Resource resourceFinancials;

    @Value("${app.documentResourceOverview}")
    private Resource resourceOverivew;

    @Value("${app.documentResourcePrices}")
    private Resource resourcePrices;

    @Bean
    public EmbeddingClient embeddingClient(){
        return  new OpenAiEmbeddingClient(new OpenAiApi(OPENAI_API_KEY),
                MetadataMode.EMBED,
                OpenAiEmbeddingOptions.builder().withModel("text-embedding-3-small").build());
//                .withDefaultOptions(OpenAiEmbeddingOptions.builder().withModel("text-embedding-3-small").build());
    }

    @Bean
    public SimpleVectorStore simpleVectorStore(EmbeddingClient embeddingClient) throws IOException, URISyntaxException {
        SimpleVectorStore simpleVectorStore = new SimpleVectorStore(embeddingClient);
        File vectorStoreFile = new File (vectorStorePath);
        resource.getFilename();
        if (vectorStoreFile.exists()){
            logger.info("vectorStoreFile exists, reusing existing " + vectorStoreFile.getAbsolutePath());
            simpleVectorStore.load(vectorStoreFile);
        }else {

            simpleVectorStore.add(generateSplitDocuments(resource));
            simpleVectorStore.add(generateSplitDocuments(resourceEstimates));
            simpleVectorStore.add(generateSplitDocuments(resourceFinancials));
            simpleVectorStore.add(generateSplitDocuments(resourceOverivew));
            simpleVectorStore.add(generateSplitDocuments(resourcePrices));
            simpleVectorStore.save(vectorStoreFile);
        }
        return simpleVectorStore;
    }

    private List<Document> generateSplitDocuments(Resource resource) throws IOException {
        logger.info("generating new vectorStoreFile from resource " + resource.getURI());
        TikaDocumentReader documentReader = new TikaDocumentReader(resource);
        List<Document> documents = documentReader.get();
        TextSplitter textSplitter = new TokenTextSplitter();
        List<Document> splitDocuments = textSplitter.apply(documents);
        return splitDocuments;
    }

}
