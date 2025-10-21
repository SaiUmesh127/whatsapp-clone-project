package com.accenture.whatsapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * ENTITY: LoginAttempt
 * Purpose: Track all login attempts (success/failure) for security
 * Database Table: login_attempts
 */
@Entity
@Table(name = "login_attempts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginAttempt {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // User who attempted login
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    
    // Username/Email used for login attempt
    @Column(nullable = false)
    private String attemptedUsername;
    
    // Was login successful?
    @Column(nullable = false)
    private Boolean successful;
    
    // IP Address of the attempt
    @Column(length = 50)
    private String ipAddress;
    
    // User Agent (Browser/Device info)
    @Column(length = 500)
    private String userAgent;
    
    // Timestamp of attempt
    @Column(nullable = false)
    private LocalDateTime attemptedAt;
    
    // Failure reason (if failed)
    @Column(length = 200)
    private String failureReason;
    
    @PrePersist
    protected void onCreate() {
        attemptedAt = LocalDateTime.now();
    }
}