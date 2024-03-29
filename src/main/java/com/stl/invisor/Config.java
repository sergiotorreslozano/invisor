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
import java.util.Arrays;
import java.util.List;

@Configuration
public class Config {

    Logger logger = LoggerFactory.getLogger(Config.class);

    @Value("${app.vectorstore.path:/tmp/vectorstore.json}")
    private String vectorStorePath;

    @Value("${app.documentResource}")
    private String documentResource;

    @Value("${spring.ai.openai.api-key:-forTesting}")
    private String OPENAI_API_KEY;

    @Value("${app.folder.path:classpath:tep/*}")
    private Resource[] tepResources;

    @Value("${app.folder.path:classpath:paypal/*}")
    private Resource[] paypalResources;


    @Bean
    public EmbeddingClient embeddingClient(){
        return  new OpenAiEmbeddingClient(new OpenAiApi(OPENAI_API_KEY),
                MetadataMode.EMBED,
                OpenAiEmbeddingOptions.builder().withModel("text-embedding-3-small").build());
    }

    @Bean
    public SimpleVectorStore simpleVectorStore(EmbeddingClient embeddingClient) throws IOException {
        SimpleVectorStore simpleVectorStore = new SimpleVectorStore(embeddingClient);
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
        return simpleVectorStore;
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

}
