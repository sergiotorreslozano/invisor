package com.stl.invisor.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "APP_USER")
public class User extends BaseEntity {

    @Id
    @Column(name = "uuid", nullable = false, updatable = false)
    private UUID uuid;

    @Column
    private String name;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole userRole;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Question> questions = new ArrayList<>();

    public User() {}

    // Private constructor
    private User(Builder builder) {
        this.uuid = builder.uuid != null ? builder.uuid : UUID.randomUUID(); // Generate UUID only if not set
        this.name = builder.name;
        this.isActive = builder.isActive;
        this.userRole = builder.userRole;
        this.questions = builder.questions; // Assign questions to the user

        // Set inherited fields from BaseEntity
        setCreatedTime(builder.createdTime);
        setUpdateTime(builder.updateTime);
        setCreatedBy(builder.createdBy);
        setUpdatedBy(builder.updatedBy);
    }

    // Getters and setters
    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    // Static inner Builder class
    public static class Builder {
        private UUID uuid;
        private String name;
        private boolean isActive = true;
        private UserRole userRole;

        // Inherited fields from BaseEntity
        private Instant createdTime;
        private Instant updateTime;
        private UUID createdBy;
        private UUID updatedBy;

        private List<Question> questions = new ArrayList<>(); // Initialize questions list

        public Builder() {}

        // Builder method for userRole
        public Builder withUserRole(UserRole userRole) {
            this.userRole = userRole;
            return this;
        }

        // Builder method for isActive
        public Builder withIsActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        // Builder method to set the UUID
        public Builder withUuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        // Builder method to set the name
        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        // Builder method to set questions
        public Builder withQuestions(List<Question> questions) {
            this.questions = questions;
            return this;
        }

        // Add a question to the user
        public Builder addQuestion(Question question) {
            this.questions.add(question);
            question.setUser(this.build()); // Set the User on the Question side
            return this;
        }

        // Builder methods for inherited fields
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

        // Build method to create a User object
        public User build() {
            return new User(this);
        }
    }

    // Optional: Add a static builder() method to easily start building
    public static Builder builder() {
        return new Builder();
    }
}
