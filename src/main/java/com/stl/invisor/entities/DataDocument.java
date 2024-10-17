package com.stl.invisor.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "DATA_DOCUMENT")
public class DataDocument extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String name;

    @Column
    private String folder;

    // One dataDocument can have multiple dataDocumentPart
    @OneToMany(mappedBy = "dataDocument", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<DataDocumentPart> dataDocumentParts;

    public DataDocument() {
    }

    // Private constructor to enforce the use of the builder
    private DataDocument(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.dataDocumentParts = builder.dataDocumentParts;
        this.folder = builder.folder;
        setCreatedTime(builder.createdTime);
        setUpdateTime(builder.updateTime);
        setCreatedBy(builder.createdBy);
        setUpdatedBy(builder.updatedBy);
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFolder() {
        return folder;
    }

    public List<DataDocumentPart> getDataDocumentParts() {
        return dataDocumentParts;
    }

    public void setDataDocumentParts(List<DataDocumentPart> dataDocumentParts) {
        this.dataDocumentParts = dataDocumentParts;
    }

    // Static builder class
    public static class Builder {
        private UUID id;
        private String name;
        private String folder;
        private List<DataDocumentPart> dataDocumentParts;
        private Instant createdTime;
        private Instant updateTime;
        private UUID createdBy;
        private UUID updatedBy;

        // Methods to set each field
        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder folder(String folder){
            this.folder = folder;
            return this;
        }

        public Builder dataDocumentParts(List<DataDocumentPart> dataDocumentParts) {
            this.dataDocumentParts = dataDocumentParts;
            return this;
        }

        public Builder createdTime(Instant createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public Builder updateTime(Instant updateTime) {
            this.updateTime = updateTime;
            return this;
        }

        public Builder createdBy(UUID createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder updatedBy(UUID updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        // Build method to create an instance of DataDocument
        public DataDocument build() {
            return new DataDocument(this);
        }
    }

    // Optional: toString, equals, hashCode methods for debugging and comparison
    @Override
    public String toString() {
        return "DataDocument{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", folder='" + folder + '\'' +
                ", dataDocumentParts=" + dataDocumentParts +
                '}';
    }
}
