package com.accenture.whatsapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO: MessageDto
 * Purpose: Transfer message data between layers
 */
@Data
public class MessageDto {
    
    private Long id;
    
    @NotNull(message = "Receiver ID is required")
    private Long receiverId;
    
    @NotBlank(message = "Message content is required")
    private String content;
    
    private String messageType = "TEXT";
    
    // For response
    private Long senderId;
    private String senderName;
    private String receiverName;
    private String sentAt;
    private Boolean isRead;

    // ✨ NEW FIELD - ADD THIS
    private String reaction;
}