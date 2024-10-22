package com.stl.invisor.dto;

import com.stl.invisor.entities.DataDocument;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DataDocumentDto extends BaseDto {
    private UUID id;
    private String name;
    private String folder;
    private List<DataDocumentPartDto> dataDocumentParts;

    // Constructor that accepts a DataDocument entity
    public DataDocumentDto(DataDocument dataDocument) {
        this.id = dataDocument.getId();
        this.name = dataDocument.getName();
        this.folder = dataDocument.getFolder();

        // Set the BaseDto fields (createdTime, updatedTime, etc.)
        this.setCreatedTime(dataDocument.getCreatedTime());
        this.setUpdateTime(dataDocument.getUpdateTime());
        this.setCreatedBy(dataDocument.getCreatedBy());
        this.setUpdatedBy(dataDocument.getUpdatedBy());

        // Convert List<DataDocumentPart> to List<DataDocumentPartDto>
        this.dataDocumentParts = dataDocument.getDataDocumentParts()
                .stream()
                .map(DataDocumentPartDto::new)  // Convert each DataDocumentPart to DataDocumentPartDto
                .collect(Collectors.toList());
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public List<DataDocumentPartDto> getDataDocumentParts() {
        return dataDocumentParts;
    }

    public void setDataDocumentParts(List<DataDocumentPartDto> dataDocumentParts) {
        this.dataDocumentParts = dataDocumentParts;
    }

    // Optional: toString method for debugging
    @Override
    public String toString() {
        return "DataDocumentDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", folder='" + folder + '\'' +
                ", dataDocumentParts=" + dataDocumentParts +
                '}';
    }
}
