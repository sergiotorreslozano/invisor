package com.stl.invisor.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "DATA_DOCUMENT_PART")
public class DataDocumentPart extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_document_id")
    private DataDocument dataDocument;

}
