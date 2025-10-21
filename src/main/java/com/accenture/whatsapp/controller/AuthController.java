package com.accenture.whatsapp.controller;

import com.accenture.whatsapp.dto.AuthResponse;
import com.accenture.whatsapp.dto.LoginRequest;
import com.accenture.whatsapp.dto.RegisterRequest;
import com.accenture.whatsapp.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            log.info("Registration request received for username: {}", request.getUsername());
            AuthResponse response = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Registration failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        try {
            log.info("Login request received for username: {}", request.getUsername());
            AuthResponse response = authService.login(request, httpRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Login failed for username: {} - {}", request.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            log.info("Logout request received for user: {}", userDetails.getUsername());
            authService.logout(userDetails.getUsername());
            return ResponseEntity.ok(new SuccessResponse("Logged out successfully"));
        } catch (Exception e) {
            log.error("Logout failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(new SuccessResponse("Current user: " + userDetails.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    @PostMapping("/register-multiple")
    public ResponseEntity<?> registerMultiple(@Valid @RequestBody List<RegisterRequest> requests) {
        try {
            log.info("Bulk registration request received for {} users", requests.size());
            List<AuthResponse> responses = authService.registerMultiple(requests);
            log.info("Successfully registered {} users", responses.size());
            return ResponseEntity.status(HttpStatus.CREATED).body(responses);
        } catch (Exception e) {
            log.error("Bulk registration failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    @lombok.Data
    @lombok.AllArgsConstructor
    static class ErrorResponse {
        private String error;
    }
    
    @lombok.Data
    @lombok.AllArgsConstructor
    static class SuccessResponse {
        private String message;
    }
}



/*package com.accenture.whatsapp.controller;

import com.accenture.whatsapp.dto.AuthResponse;
import com.accenture.whatsapp.dto.LoginRequest;
import com.accenture.whatsapp.dto.RegisterRequest;
import com.accenture.whatsapp.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * CONTROLLER: AuthController
 * Purpose: Handle authentication endpoints (register, login, logout)
 * Base URL: /api/auth
 * 
 * ENDPOINTS:
 * POST /api/auth/register - Register new user
 * POST /api/auth/login - Login user (sends email notification)
 * POST /api/auth/logout - Logout user
 
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    /**
     * REGISTER NEW USER
     * URL: POST /api/auth/register
     * Request Body: RegisterRequest (JSON)
     * Response: AuthResponse with JWT token
     * 
     * Example Request:
     * {
     *   "username": "sai_umesh",
     *   "email": "sai@example.com",
     *   "password": "password123",
     *   "fullName": "Sai Umesh Narahari",
     *   "phoneNumber": "+91 9876543210"
     * }
     
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            log.info("Registration request received for username: {}", request.getUsername());
            
            AuthResponse response = authService.register(request);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            log.error("Registration failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * LOGIN USER
     * URL: POST /api/auth/login
     * Request Body: LoginRequest (JSON)
     * Response: AuthResponse with JWT token
     * 
     * IMPORTANT: Sends email notification on every login attempt!
     * - Success: User receives "Login Successful" email
     * - Failure: User receives "Failed Login Attempt" email
     * 
     * Example Request:
     * {
     *   "username": "sai_umesh",
     *   "password": "password123"
     * }
     * 
     * Example Response (Success):
     * {
     *   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
     *   "type": "Bearer",
     *   "userId": 1,
     *   "username": "sai_umesh",
     *   "email": "sai@example.com",
     *   "fullName": "Sai Umesh Narahari",
     *   "message": "Login successful"
     * }
     
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        
        try {
            log.info("Login request received for username: {}", request.getUsername());
            
            // Authenticate user and get response
            // This will send email notification automatically
            AuthResponse response = authService.login(request, httpRequest);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Login failed for username: {} - {}", request.getUsername(), e.getMessage());
            
            // Return error response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * LOGOUT USER
     * URL: POST /api/auth/logout
     * Headers: Authorization: Bearer <token>
     * Response: Success message
     * 
     * Updates user status to offline
     
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            log.info("Logout request received for user: {}", userDetails.getUsername());
            
            authService.logout(userDetails.getUsername());
            
            return ResponseEntity.ok(new SuccessResponse("Logged out successfully"));
            
        } catch (Exception e) {
            log.error("Logout failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * GET CURRENT USER
     * URL: GET /api/auth/me
     * Headers: Authorization: Bearer <token>
     * Response: Current user details
     
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            return ResponseEntity.ok(new SuccessResponse("Current user: " + userDetails.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * ✨ NEW ENDPOINT: REGISTER MULTIPLE USERS AT ONCE
     * URL: POST /api/auth/register-multiple
     * Request Body: List<RegisterRequest> (JSON array)
     * Response: List of created users
     * 
     * Example Request:
     * [
     *   {
     *     "username": "user1",
     *     "email": "user1@example.com",
     *     "password": "password123",
     *     "fullName": "User One",
     *     "phoneNumber": "+91 1111111111"
     *   },
     *   {
     *     "username": "user2",
     *     "email": "user2@example.com",
     *     "password": "password123",
     *     "fullName": "User Two",
     *     "phoneNumber": "+91 2222222222"
     *   }
     * ]
     
    @PostMapping("/register-multiple")
    public ResponseEntity<?> registerMultiple(@Valid @RequestBody List<RegisterRequest> requests) {
        try {
            log.info("Bulk registration request received for {} users", requests.size());
            
            // Call service to register multiple users
            List<AuthResponse> responses = authService.registerMultiple(requests);
            
            log.info("Successfully registered {} users", responses.size());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(responses);
            
        } catch (Exception e) {
            log.error("Bulk registration failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    // Helper classes for responses
    @lombok.Data
    @lombok.AllArgsConstructor
    static class ErrorResponse {
        private String error;
    }
    
    @lombok.Data
    @lombok.AllArgsConstructor
    static class SuccessResponse {
        private String message;
    }
} */ 