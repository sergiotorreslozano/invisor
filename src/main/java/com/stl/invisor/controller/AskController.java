package com.stl.invisor.controller;


import com.stl.invisor.service.VectorStoreService;
import com.stl.invisor.dto.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AskController {

    Logger logger = LoggerFactory.getLogger(AskController.class);

    private final ChatClient aiClient;
    private final VectorStoreService vectorStoreService;

    @Value("classpath:/rag-prompt-template.st")
    private Resource ragPromptTemplate;

    @Autowired
    public AskController(ChatClient aiClient, VectorStoreService vectorStoreService){
        this.aiClient = aiClient;
        this.vectorStoreService = vectorStoreService;
    }

    @GetMapping("/ask")
    public Answer ask(@RequestParam("question") String question){

        SearchRequest searchRequest =  SearchRequest.query(question).withTopK(2);
        List<Document> documents = vectorStoreService.similaritySearch(searchRequest);
        logger.info("documents size: " + String.valueOf(documents.size()));
        List<String> contentList = documents.stream().map(Document::getContent).toList();
        PromptTemplate promptTemplate = new PromptTemplate(ragPromptTemplate);
        Map<String, Object> promptParameters = new HashMap<>();
        promptParameters.put("input", question);
        promptParameters.put("documents", String.join("\n", contentList));

        Prompt prompt = promptTemplate.create(promptParameters);
        ChatResponse response = aiClient.call(prompt);
        logger.info(response.getMetadata().getUsage().toString());
        Answer answer = new Answer(response.getResult().getOutput().getContent());
        logger.info(searchRequest.toString());
        logger.info(answer.toString());
        return new Answer(response.getResult().getOutput().getContent());
    }

}

