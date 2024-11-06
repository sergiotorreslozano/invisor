package com.stl.invisor.controller;

import com.stl.invisor.dto.Answer;
import com.stl.invisor.dto.QuestionDto;
import com.stl.invisor.dto.UserDto;
import com.stl.invisor.entities.Question;
import com.stl.invisor.entities.User;
import com.stl.invisor.repository.QuestionRepository;
import com.stl.invisor.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    public UserController(UserRepository userRepository, QuestionRepository questionRepository){
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
    }

    @GetMapping("/api/users")
    public ResponseEntity<List<UserDto>> findUsers (){
        logger.info("Returning all users");
        List<User> users = userRepository.findAll();
        List<UserDto> usersDto = users.stream()
                .map((UserDto::new))
                .collect(Collectors.toList());
        return  ResponseEntity.ok(usersDto);
    }


    @GetMapping("/api/users/{userId}/questions")
    public ResponseEntity<List<QuestionDto>> findUserQuestions(@PathVariable String userId){
        logger.info("Returning questions for user: {}", userId);
        List<Question> questions = questionRepository.findQuestionsByUserUuid(UUID.fromString(userId));

        // Map the list of Question entities to QuestionDto using the constructor
        List<QuestionDto> questionsDto = questions.stream()
                .map(QuestionDto::new)  // Directly map using the new constructor
                .collect(Collectors.toList());

        return ResponseEntity.ok(questionsDto);
    }

    @GetMapping("/api/users/{userId}/folders/{folder}/questions")
    public ResponseEntity<List<QuestionDto>> findUserQuestionsByFolder(@PathVariable String userId, @PathVariable String folder){
        logger.info("Returning questions for user: {} and folder: {} ", userId, folder);
        List<Question> questions = questionRepository.findQuestionsByUserUuidAndFolder(UUID.fromString(userId), folder);

        // Map the list of Question entities to QuestionDto using the constructor
        List<QuestionDto> questionsDto = questions.stream()
                .map(QuestionDto::new)  // Directly map using the new constructor
                .collect(Collectors.toList());

        return ResponseEntity.ok(questionsDto);
    }
}
