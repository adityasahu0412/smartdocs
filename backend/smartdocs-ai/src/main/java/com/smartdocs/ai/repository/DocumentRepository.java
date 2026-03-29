package com.smartdocs.ai.repository;

import com.smartdocs.ai.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

// no custom queries needed here
// JpaRepository gives us save, findAll, findById, delete for free

public interface DocumentRepository extends JpaRepository<Document, Long> {
}