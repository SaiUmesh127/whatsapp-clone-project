package com.accenture.whatsapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO: LoginRequest
 * Purpose: Receives login credentials from client
 */
@Data
public class LoginRequest {
    
    @NotBlank(message = "Username is required")
    private String username;
    
    @NotBlank(message = "Password is required")
    private String password;
}