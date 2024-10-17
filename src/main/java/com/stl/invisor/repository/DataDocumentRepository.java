package com.stl.invisor.repository;

import com.stl.invisor.entities.DataDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DataDocumentRepository extends JpaRepository<DataDocument, UUID> {

    @Query("SELECT d FROM DataDocument d LEFT JOIN FETCH d.dataDocumentParts WHERE d.folder = :folder")
    List<DataDocument> findDataDocumentsByFolderWithParts(@Param("folder") String folder);
}
