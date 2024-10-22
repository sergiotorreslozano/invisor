package com.stl.invisor.entities;

import jakarta.persistence.*;
import java.util.UUID;
import java.time.Instant;

@Entity
@Table(name = "QUESTIONS")
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(length = 10485760)
    private String question;

    @Column(length = 10485760)
    private String answer;

    private String folder;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @Enumerated(EnumType.STRING)
    private QuestionStatus questionStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Question() {}

    public UUID getUuid() {
        return uuid;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getFolder() {
        return folder;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public QuestionStatus getQuestionStatus() {
        return questionStatus;
    }

    public User getUser() {
        return user;
    }

    // Private constructor
    private Question(Builder builder) {
        this.uuid = builder.uuid;
        this.question = builder.question;
        this.answer = builder.answer;
        this.questionType = builder.questionType;
        this.user = builder.user;
        this.folder = builder.folder;
        this.questionStatus = builder.questionStatus;

        // Set inherited BaseEntity fields
        setCreatedTime(builder.createdTime);
        setUpdateTime(builder.updateTime);
        setCreatedBy(builder.createdBy);
        setUpdatedBy(builder.updatedBy);
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Static inner Builder class
    public static class Builder {
        private UUID uuid;
        private String question;
        private String answer;
        private String folder;
        private QuestionType questionType;
        private QuestionStatus questionStatus;
        private User user;

        // Fields from BaseEntity
        private Instant createdTime;
        private Instant updateTime;
        private UUID createdBy;
        private UUID updatedBy;

        // Default empty constructor
        public Builder() {}

        // Constructor that takes an existing Question and copies its values
        public Builder(Question existingQuestion) {
            this.uuid = existingQuestion.uuid;
            this.question = existingQuestion.question;
            this.answer = existingQuestion.answer;
            this.folder = existingQuestion.folder;
            this.questionType = existingQuestion.questionType;
            this.questionStatus = existingQuestion.questionStatus;
            this.user = existingQuestion.user;

            // BaseEntity fields
            this.createdTime = existingQuestion.getCreatedTime();
            this.updateTime = existingQuestion.getUpdateTime();
            this.createdBy = existingQuestion.getCreatedBy();
            this.updatedBy = existingQuestion.getUpdatedBy();
        }

        // Builder method to set the UUID
        public Builder withUuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        // Builder method to set the question text
        public Builder withQuestion(String question) {
            this.question = question;
            return this;
        }

        // Builder method to set the answer text
        public Builder withAnswer(String answer) {
            this.answer = answer;
            return this;
        }

        // Builder method for questionType
        public Builder withQuestionType(QuestionType questionType) {
            this.questionType = questionType;
            return this;
        }

        public Builder withQuestionStatus(QuestionStatus questionStatus) {
            this.questionStatus = questionStatus;
            return this;
        }

        // Builder method for associated user
        public Builder withUser(User user) {
            this.user = user;
            return this;
        }

        public Builder withFolder(String folder) {
            this.folder = folder;
            return this;
        }

        // Builder methods for BaseEntity fields
        public Builder withCreatedTime(Instant createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public Builder withUpdateTime(Instant updateTime) {
            this.updateTime = updateTime;
            return this;
        }

        public Builder withCreatedBy(UUID createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder withUpdatedBy(UUID updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        // Build method to create a Question object
        public Question build() {
            return new Question(this);
        }
    }

    // Optional: Add a static builder() method to easily start building
    public static Builder builder() {
        return new Builder();
    }

    // Overloaded builder method that accepts an existing Question object
    public static Builder builder(Question existingQuestion) {
        return new Builder(existingQuestion);
    }
}
