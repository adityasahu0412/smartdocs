package com.smartdocs.ai.service;

import com.smartdocs.ai.entity.Document;
import com.smartdocs.ai.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// saves document info to database after upload

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public Document saveDocument(Document doc) {
        return documentRepository.save(doc);
    }
}