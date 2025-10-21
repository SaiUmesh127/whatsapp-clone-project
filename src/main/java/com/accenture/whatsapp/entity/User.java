package com.accenture.whatsapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * ENTITY: User
 * Purpose: Represents a user in the WhatsApp system
 * Database Table: users
 */
@Entity
@Table(name = "users")
@Data  // Lombok: Auto-generates getters, setters, toString, equals, hashCode
@NoArgsConstructor  // Lombok: Generates no-argument constructor
@AllArgsConstructor  // Lombok: Generates constructor with all fields
public class User {
    
    // Primary Key - Auto-generated ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Username - Must be unique
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    
    // Email - Must be unique (used for login notifications)
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    // Password - Will be encrypted using BCrypt
    @Column(nullable = false)
    private String password;
    
    // Full Name
    @Column(nullable = false, length = 100)
    private String fullName;
    
    // Profile Picture URL (optional)
    @Column(length = 500)
    private String profilePicture;
    
    // About/Status message
    @Column(length = 500)
    private String about;
    
    // Phone Number
    @Column(length = 20)
    private String phoneNumber;
    
    // Online Status
    @Column(nullable = false)
    private Boolean online = false;
    
    // Last Seen Timestamp
    @Column
    private LocalDateTime lastSeen;
    
    // Account Creation Timestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // Last Update Timestamp
    @Column
    private LocalDateTime updatedAt;
    
    // Is Account Active?
    @Column(nullable = false)
    private Boolean active = true;
    
    // Set timestamps before persisting
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    // Update timestamp before updating
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}