package com.accenture.whatsapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * ENTITY: Message
 * Purpose: Represents a chat message between two users
 * Database Table: messages
 */
@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    
    // Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Sender - Foreign Key to User
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;
    
    // Receiver - Foreign Key to User
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;
    
    // Message Content
    @Column(nullable = false, length = 5000)
    private String content;
    
    // Message Type (TEXT, IMAGE, VIDEO, FILE)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType messageType = MessageType.TEXT;
    
    // Is message read?
    @Column(nullable = false)
    private Boolean isRead = false;
    
    // Timestamp when message was sent
    @Column(nullable = false)
    private LocalDateTime sentAt;
    
    // Timestamp when message was read
    @Column
    private LocalDateTime readAt;

    // ✨ NEW FIELD - ADD THIS
    @Column(length = 20)
    private String reaction;
    
    // Set timestamp before persisting
    @PrePersist
    protected void onCreate() {
        sentAt = LocalDateTime.now();
    }
    
    // Message Type Enum
    public enum MessageType {
        TEXT, IMAGE, VIDEO, FILE, AUDIO
    }
}