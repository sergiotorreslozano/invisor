package com.stl.invisor.dto;

import com.stl.invisor.entities.User;
import com.stl.invisor.entities.UserRole;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserDto {

    private UUID uuid;
    private String name;
    private boolean isActive;
    private UserRole userRole;
    private List<QuestionDto> questions;  // A list of QuestionDto to represent the user's questions

    // Constructor that takes a User entity and initializes the DTO fields
    public UserDto(User user) {
        this.uuid = user.getUuid();
        this.name = user.getName();
        this.isActive = user.isActive();
        this.userRole = user.getUserRole();

        // Map each Question entity to a QuestionDto
        this.questions = user.getQuestions().stream()
                .map(QuestionDto::new)  // Using the QuestionDto constructor
                .collect(Collectors.toList());
    }

    // Getters and setters (optional if you're using Lombok or a similar library)
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public List<QuestionDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDto> questions) {
        this.questions = questions;
    }
}
