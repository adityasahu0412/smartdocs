package com.smartdocs.ai.entity;

import jakarta.persistence.*;
import lombok.*;

// stores uploaded PDF file info in the database
// actual file is saved in the uploads folder on disk

@Entity
@Data               // generates getters, setters, toString
@NoArgsConstructor  // generates empty constructor
@AllArgsConstructor // generates constructor with all fields
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName; // original name of the uploaded file

    private String filePath; // full path where file is saved on disk
}