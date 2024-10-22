package com.stl.invisor.dto;

import java.time.Instant;
import java.util.UUID;

public abstract class BaseDto {
    private Instant createdTime;
    private Instant updateTime;
    private UUID createdBy;
    private UUID updatedBy;

    // Getters and Setters
    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public UUID getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(UUID updatedBy) {
        this.updatedBy = updatedBy;
    }

    // Optional: toString method for debugging
    @Override
    public String toString() {
        return "BaseDTO{" +
                "createdTime=" + createdTime +
                ", updateTime=" + updateTime +
                ", createdBy=" + createdBy +
                ", updatedBy=" + updatedBy +
                '}';
    }
}

