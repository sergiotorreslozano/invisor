package com.stl.invisor.controller;

import com.stl.invisor.dto.DataDocumentDto;
import com.stl.invisor.entities.DataDocument;
import com.stl.invisor.repository.DataDocumentPartRepository;
import com.stl.invisor.repository.DataDocumentRepository;
import com.stl.invisor.service.VectorStoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class DataDocumentController {

    private final DataDocumentRepository dataDocumentRepository;
    private final VectorStoreService vectorStoreService;

    public DataDocumentController (DataDocumentRepository dataDocumentRepository, VectorStoreService vectorStoreService){
        this.dataDocumentRepository = dataDocumentRepository;
        this.vectorStoreService = vectorStoreService;
    }

    @GetMapping("/api/documents")
    public List<DataDocumentDto> findDocuments(){
        List<DataDocument> dataDocuments = dataDocumentRepository.findAll();
        // Convert the List<DataDocument> to List<DataDocumentDto>
        return dataDocuments.stream()
                .map(DataDocumentDto::new)  // Map each DataDocument entity to a DataDocumentDto
                .collect(Collectors.toList());
    }
    @GetMapping("/api/documents/{folder}")
    public List<DataDocumentDto> findDocumentsByFolder(@PathVariable  String folder){
        List<DataDocument> dataDocuments = dataDocumentRepository.findDataDocumentsByFolderWithParts(folder);

        // Convert the List<DataDocument> to List<DataDocumentDto>
        return dataDocuments.stream()
                .map(DataDocumentDto::new)  // Map each DataDocument entity to a DataDocumentDto
                .collect(Collectors.toList());
    }

    @PostMapping("/api/documents/{folder}/reload")
    public ResponseEntity<String> reloadDocumentsFolder(@PathVariable String folder) throws IOException {

        vectorStoreService.reloadDocuments(folder);
        return ResponseEntity.ok("ok");
    }

}
