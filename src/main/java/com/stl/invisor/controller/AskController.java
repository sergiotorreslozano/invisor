package com.stl.invisor.controller;


import com.stl.invisor.dto.QuestionRecord;
import com.stl.invisor.entities.Question;
import com.stl.invisor.entities.QuestionStatus;
import com.stl.invisor.entities.QuestionType;
import com.stl.invisor.entities.User;
import com.stl.invisor.exceptions.NotFoundException;
import com.stl.invisor.repository.DataDocumentRepository;
import com.stl.invisor.repository.QuestionRepository;
import com.stl.invisor.repository.UserRepository;
import com.stl.invisor.service.VectorStoreService;
import com.stl.invisor.dto.Answer;
import jakarta.validation.Valid;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class AskController {

    Logger logger = LoggerFactory.getLogger(AskController.class);

    private final ChatClient aiClient;
    private final VectorStoreService vectorStoreService;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final DataDocumentRepository dataDocumentRepository;

    @Value("classpath:/rag-prompt-template.st")
    private Resource ragPromptTemplate;

    @Autowired
    public AskController(ChatClient aiClient, VectorStoreService vectorStoreService, QuestionRepository questionRepository, UserRepository userRepository, DataDocumentRepository dataDocumentRepository){
        this.aiClient = aiClient;
        this.vectorStoreService = vectorStoreService;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.dataDocumentRepository = dataDocumentRepository;
    }

    @GetMapping("/ask")
    public Answer ask(@RequestParam("question") String question){

        Prompt prompt = createPrompt(question);
        ChatResponse response = aiClient.call(prompt);
        logger.info(response.getMetadata().getUsage().toString());
        Answer answer = new Answer(response.getResult().getOutput().getContent());

        logger.info(answer.toString());
        return new Answer(response.getResult().getOutput().getContent());
    }

    @PostMapping("/api/users/{userId}/folders/{folder}/questions")
    public ResponseEntity<Answer> askQuestion(@PathVariable String userId, @PathVariable String folder, @Valid @RequestBody QuestionRecord questionRequest){
        User user = userRepository.findByUuid(UUID.fromString(userId));
        if (user == null){
            throw new NotFoundException("User not found");
        }
        if(!dataDocumentRepository.existsByFolder(folder)){
            throw new NotFoundException("Folder do not exists");
        }
        Question question = createQuestion(user, questionRequest, folder, QuestionType.DEFAULT);
        Prompt prompt = createPrompt(questionRequest.question());
        ChatResponse response = aiClient.call(prompt);
        logger.info(response.getMetadata().getUsage().toString());
        logger.info(response.getResult().getMetadata().toString());
        Answer answer = new Answer(response.getResult().getOutput().getContent());
        updateQuestion(question, QuestionStatus.GENERATED, answer, user);
        logger.info(answer.toString());
        return ResponseEntity.ok(answer);
    }

    private void updateQuestion(Question question, QuestionStatus questionStatus, Answer answer, User user) {
        Question q = Question.builder(question)
                .withQuestionStatus(questionStatus)
                .withAnswer(answer.answer())
                .withUpdatedBy(user.getUuid())
                .withUpdateTime(Instant.now())
                .withUuid(question.getUuid())
                .build();
        questionRepository.save(q);
    }

    private Question createQuestion(User user, QuestionRecord questionRequest, String folder, QuestionType questionType) {
        Question question = Question.builder()
                .withQuestion(questionRequest.question())
                .withFolder(folder)
                .withUser(user)
                .withQuestionStatus(QuestionStatus.REQUESTED)
                .withQuestionType(questionType)
                .withCreatedTime(Instant.now())
                .withCreatedBy(user.getUuid())
                .build();
        questionRepository.save(question);
        return question;
    }

    private Prompt createPrompt (String question){
        logger.info("Question: " + question);

        SearchRequest searchRequest =  SearchRequest.query(question).withTopK(6);
        logger.info(searchRequest.toString());
        List<Document> documents = vectorStoreService.similaritySearch(searchRequest);
        logger.info("Documents size: " + String.valueOf(documents.size()));
        List<String> contentList = documents.stream().map(Document::getContent).toList();
        PromptTemplate promptTemplate = new PromptTemplate(ragPromptTemplate);
        Map<String, Object> promptParameters = new HashMap<>();
        promptParameters.put("input", question);
        promptParameters.put("documents", String.join("\n", contentList));

        return promptTemplate.create(promptParameters);
    }

}

