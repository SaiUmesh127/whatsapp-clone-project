package com.accenture.whatsapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO: AuthResponse
 * Purpose: Sends authentication response to client
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    
    private String token;  // JWT token
    private String type = "Bearer";  // Token type
    private Long userId;
    private String username;
    private String email;
    private String fullName;
    private String message;
}