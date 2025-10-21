package com.accenture.whatsapp.service;

import com.accenture.whatsapp.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * SERVICE: EmailService
 * Purpose: Send email notifications for login attempts
 * Flow:
 * 1. Called after every login attempt (success or failure)
 * 2. Sends HTML email with details
 * 3. Runs asynchronously (doesn't block the login response)
 */
@Service
@Slf4j  // Lombok: Provides logging
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private TemplateEngine templateEngine;
    
    @Value("${app.email.from}")
    private String fromEmail;
    
    @Value("${app.email.name}")
    private String fromName;
    
    /**
     * Send email notification for SUCCESSFUL login
     * @param user - User who logged in
     * @param ipAddress - IP address of login
     * @param userAgent - Browser/Device info
     */
    @Async  // Runs in separate thread, doesn't block login response
    public void sendLoginSuccessEmail(User user, String ipAddress, String userAgent) {
        try {
            log.info("Sending login SUCCESS email to: {}", user.getEmail());
            
            // Create email context with data
            Context context = new Context();
            context.setVariable("username", user.getUsername());
            context.setVariable("fullName", user.getFullName());
            context.setVariable("loginTime", formatDateTime(LocalDateTime.now()));
            context.setVariable("ipAddress", ipAddress);
            context.setVariable("device", parseDevice(userAgent));
            context.setVariable("browser", parseBrowser(userAgent));
            
            // Process HTML template
            String htmlContent = templateEngine.process("login-success-email", context);
            
            // Send email
            sendHtmlEmail(user.getEmail(), "✅ Successful Login Alert - WhatsApp Clone", htmlContent);
            
            log.info("Login SUCCESS email sent successfully to: {}", user.getEmail());
            
        } catch (Exception e) {
            log.error("Error sending login success email: {}", e.getMessage());
        }
    }
    
    /**
     * Send email notification for FAILED login attempt
     * @param email - Email address used in failed attempt
     * @param username - Username used in failed attempt
     * @param reason - Reason for failure
     * @param ipAddress - IP address of attempt
     * @param userAgent - Browser/Device info
     */
    @Async  // Runs in separate thread
    public void sendLoginFailureEmail(String email, String username, String reason, 
                                     String ipAddress, String userAgent) {
        try {
            log.info("Sending login FAILURE email to: {}", email);
            
            // Create email context with data
            Context context = new Context();
            context.setVariable("username", username);
            context.setVariable("attemptTime", formatDateTime(LocalDateTime.now()));
            context.setVariable("ipAddress", ipAddress);
            context.setVariable("device", parseDevice(userAgent));
            context.setVariable("browser", parseBrowser(userAgent));
            context.setVariable("reason", reason);
            
            // Process HTML template
            String htmlContent = templateEngine.process("login-failure-email", context);
            
            // Send email
            sendHtmlEmail(email, "⚠️ Failed Login Attempt Alert - WhatsApp Clone", htmlContent);
            
            log.info("Login FAILURE email sent successfully to: {}", email);
            
        } catch (Exception e) {
            log.error("Error sending login failure email: {}", e.getMessage());
        }
    }
    
    /**
     * Send HTML email
     * @throws UnsupportedEncodingException 
     */
    private void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setFrom(fromEmail, fromName);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);  // true = HTML content
        
        mailSender.send(message);
    }
    
    /**
     * Format LocalDateTime to readable string
     */
    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm:ss a");
        return dateTime.format(formatter);
    }
    
    /**
     * Parse device information from User-Agent string
     */
    private String parseDevice(String userAgent) {
        if (userAgent == null) return "Unknown Device";
        
        if (userAgent.contains("Mobile")) {
            return "Mobile Device";
        } else if (userAgent.contains("Tablet")) {
            return "Tablet";
        } else if (userAgent.contains("Windows")) {
            return "Windows PC";
        } else if (userAgent.contains("Mac")) {
            return "Mac";
        } else if (userAgent.contains("Linux")) {
            return "Linux";
        }
        return "Unknown Device";
    }
    
    /**
     * Parse browser information from User-Agent string
     */
    private String parseBrowser(String userAgent) {
        if (userAgent == null) return "Unknown Browser";
        
        if (userAgent.contains("Edg")) {
            return "Microsoft Edge";
        } else if (userAgent.contains("Chrome")) {
            return "Google Chrome";
        } else if (userAgent.contains("Firefox")) {
            return "Mozilla Firefox";
        } else if (userAgent.contains("Safari")) {
            return "Safari";
        } else if (userAgent.contains("Opera")) {
            return "Opera";
        }
        return "Unknown Browser";
    }
}