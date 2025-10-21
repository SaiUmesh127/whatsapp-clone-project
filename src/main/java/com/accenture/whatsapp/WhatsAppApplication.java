package com.accenture.whatsapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * MAIN APPLICATION CLASS
 * Purpose: Entry point for Spring Boot application
 * 
 * @SpringBootApplication includes:
 * - @Configuration: Makes this a configuration class
 * - @EnableAutoConfiguration: Auto-configures Spring
 * - @ComponentScan: Scans for components in this package
 * 
 * @EnableAsync: Enables asynchronous method execution
 * (Required for sending emails asynchronously)
 */
@SpringBootApplication
@EnableAsync  // Enable async email sending
public class WhatsAppApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(WhatsAppApplication.class, args);
        
        System.out.println("\n" +
                "╔══════════════════════════════════════════════════════╗\n" +
                "║                                                      ║\n" +
                "║          WhatsApp Clone API Started! ✅              ║\n" +
                "║                                                      ║\n" +
                "║  Server running at: http://localhost:8080           ║\n" +
                "║                                                      ║\n" +
                "║  API Endpoints:                                      ║\n" +
                "║  POST /api/auth/register - Register user            ║\n" +
                "║  POST /api/auth/login    - Login (sends email)      ║\n" +
                "║  POST /api/auth/logout   - Logout                   ║\n" +
                "║  GET  /api/users/profile - Get profile              ║\n" +
                "║  POST /api/messages      - Send message             ║\n" +
                "║                                                      ║\n" +
                "║  Created by: Sai Umesh Narahari                     ║\n" +
                "║                                                      ║\n" +
                "╚══════════════════════════════════════════════════════╝\n");
    }
}