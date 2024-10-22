package com.stl.invisor.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "DATA_DOCUMENT_PART")
public class DataDocumentPart extends BaseEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_document_id")
    private DataDocument dataDocument;

    public DataDocumentPart() {
    }

    // Private constructor to enforce the use of the builder
    private DataDocumentPart(Builder builder) {
        this.id = builder.id;
        this.dataDocument = builder.dataDocument;
        setCreatedTime(builder.createdTime);
        setUpdateTime(builder.updateTime);
        setCreatedBy(builder.createdBy);
        setUpdatedBy(builder.updatedBy);
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public DataDocument getDataDocument() {
        return dataDocument;
    }

    // Static builder class
    public static class Builder {
        private UUID id;
        private DataDocument dataDocument;
        private Instant createdTime;
        private Instant updateTime;
        private UUID createdBy;
        private UUID updatedBy;

        // Methods to set each field
        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder dataDocument(DataDocument dataDocument) {
            this.dataDocument = dataDocument;
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

        // Build method to create an instance of DataDocumentPart
        public DataDocumentPart build() {
            return new DataDocumentPart(this);
        }
    }

    // Optional: toString, equals, hashCode methods for debugging and comparison
    @Override
    public String toString() {
        return "DataDocumentPart{" +
                "id=" + id +
                ", dataDocument=" + (dataDocument != null ? dataDocument.getId() : null) + // Show only the ID to avoid lazy-loading dataDocument details
                '}';
    }
}
