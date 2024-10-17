package com.stl.invisor.repository;

import com.stl.invisor.entities.DataDocumentPart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DataDocumentPartRepository extends JpaRepository<DataDocumentPart, UUID> {
}
