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
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
@ConfigurationProperties
public class Config {

    Logger logger = LoggerFactory.getLogger(Config.class);

    @Value("${spring.ai.openai.api-key:-forTesting}")
    private String OPENAI_API_KEY;


    @Bean
    public EmbeddingClient embeddingClient(){
        return  new OpenAiEmbeddingClient(new OpenAiApi(OPENAI_API_KEY),
                MetadataMode.EMBED,
                OpenAiEmbeddingOptions.builder().withModel("text-embedding-3-small").build());
    }

}
