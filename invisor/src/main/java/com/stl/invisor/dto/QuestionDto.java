package com.stl.invisor.dto;

import com.stl.invisor.entities.Question;
import com.stl.invisor.entities.QuestionStatus;
import com.stl.invisor.entities.QuestionType;

import java.util.UUID;

public class QuestionDto {

    private UUID uuid;
    private String question;
    private String answer;
    private String folder;
    private QuestionType questionType;
    private QuestionStatus questionStatus;
    private UUID userUuid;

    // Constructor that takes a Question entity
    public QuestionDto(Question questionEntity) {
        this.uuid = questionEntity.getUuid();
        this.question = questionEntity.getQuestion();
        this.answer = questionEntity.getAnswer();
        this.folder = questionEntity.getFolder();
        this.questionType = questionEntity.getQuestionType();
        this.questionStatus = questionEntity.getQuestionStatus();
        this.userUuid = questionEntity.getUser() != null ? questionEntity.getUser().getUuid() : null;
    }

    // Getters and setters (optional if you're using Lombok or similar library)
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public QuestionStatus getQuestionStatus() {
        return questionStatus;
    }

    public void setQuestionStatus(QuestionStatus questionStatus) {
        this.questionStatus = questionStatus;
    }

    public UUID getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(UUID userUuid) {
        this.userUuid = userUuid;
    }
}
