package com.stl.invisor.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum QuestionStatus {
        @JsonProperty("requested")
        REQUESTED,
        @JsonProperty("generated")
        GENERATED,
        @JsonProperty("error")
        ERROR

}
