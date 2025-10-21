package com.accenture.whatsapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationDto {
    private Long userId;
    private String username;
    private String fullName;
    private String profilePicture;
    private String lastMessage;
    private String lastMessageTime;
    private Long unreadCount;
    private Boolean online;
}