package com.smartdocs.ai.entity;

import jakarta.persistence.*;
import lombok.*;

// user entity - stores name, email and password
// email must be unique, no two users can have same email

@Entity
@Data               // generates getters, setters, toString
@NoArgsConstructor  // generates empty constructor
@AllArgsConstructor // generates constructor with all fields
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email; // used for login, must be unique

    @Column(nullable = false)
    private String password;
}