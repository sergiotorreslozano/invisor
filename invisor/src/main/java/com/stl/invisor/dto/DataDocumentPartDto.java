package com.stl.invisor.dto;

import com.stl.invisor.entities.DataDocumentPart;

import java.util.UUID;

public class DataDocumentPartDto extends BaseDto {
    private UUID id;
    private UUID dataDocumentId;

    // Constructor that accepts a DataDocumentPart entity
    public DataDocumentPartDto(DataDocumentPart entity) {
        this.id = entity.getId();
        this.dataDocumentId = (entity.getDataDocument() != null) ? entity.getDataDocument().getId() : null;

        // Set the common fields from the entity into the BaseDTO fields
        this.setCreatedTime(entity.getCreatedTime());
        this.setUpdateTime(entity.getUpdateTime());
        this.setCreatedBy(entity.getCreatedBy());
        this.setUpdatedBy(entity.getUpdatedBy());
    }

    // Getters and Setters for DataDocumentPartDTO specific fields
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDataDocumentId() {
        return dataDocumentId;
    }

    public void setDataDocumentId(UUID dataDocumentId) {
        this.dataDocumentId = dataDocumentId;
    }

    // Optional: toString method for debugging
    @Override
    public String toString() {
        return "DataDocumentPartDTO{" +
                "id=" + id +
                ", dataDocumentId=" + dataDocumentId +
                ", createdTime=" + getCreatedTime() +
                ", updateTime=" + getUpdateTime() +
                ", createdBy=" + getCreatedBy() +
                ", updatedBy=" + getUpdatedBy() +
                '}';
    }
}

