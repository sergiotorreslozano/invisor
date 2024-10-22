package com.stl.invisor.dto;

import jakarta.validation.constraints.NotBlank;

public record QuestionRecord(
        @NotBlank(message = "Question cannot be empty") String question
) {
}
