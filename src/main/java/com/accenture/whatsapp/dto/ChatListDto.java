package com.accenture.whatsapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO: ChatListDto
 * Purpose: Used to represent chat list data for each contact
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatListDto {

    private Long userId;
    private String username;
    private String fullName;
    private String profilePicture;
    private String about;
    private Boolean online;
    private String lastSeen;
    private String lastMessage;
    private String lastMessageTime;
    private boolean lastMessageFromMe;
    private long unreadCount;
}
