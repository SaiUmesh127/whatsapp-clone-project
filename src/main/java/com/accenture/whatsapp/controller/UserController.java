package com.accenture.whatsapp.controller;

import com.accenture.whatsapp.entity.User;
import com.accenture.whatsapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * CONTROLLER: UserController
 * Purpose: Handle user-related operations
 * Base URL: /api/users
 * All endpoints require JWT authentication
 * 
 * ENDPOINTS:
 * GET /api/users/profile - Get current user profile
 * GET /api/users/{id} - Get user by ID
 * GET /api/users/search?keyword=xyz - Search users
 * GET /api/users/online - Get online users
 * PUT /api/users/profile - Update profile
 */
@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * GET CURRENT USER PROFILE
     * URL: GET /api/users/profile
     * Headers: Authorization: Bearer <token>
     */
    @GetMapping("/profile")
    public ResponseEntity<User> getCurrentUserProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("Getting profile for user: {}", userDetails.getUsername());
        
        User user = userService.getUserByUsername(userDetails.getUsername());
        return ResponseEntity.ok(user);
    }
    
    /**
     * GET USER BY ID
     * URL: GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.info("Getting user by ID: {}", id);
        
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    /**
     * SEARCH USERS
     * URL: GET /api/users/search?keyword=sai
     */
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String keyword) {
        log.info("Searching users with keyword: {}", keyword);
        
        List<User> users = userService.searchUsers(keyword);
        return ResponseEntity.ok(users);
    }
    
    /**
     * GET ONLINE USERS
     * URL: GET /api/users/online
     */
    @GetMapping("/online")
    public ResponseEntity<List<User>> getOnlineUsers() {
        log.info("Getting online users");
        
        List<User> users = userService.getOnlineUsers();
        return ResponseEntity.ok(users);
    }
    
    /**
     * UPDATE PROFILE
     * URL: PUT /api/users/profile
     * Request Body: User (JSON with fields to update)
     */
    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody User updatedUser) {
        
        log.info("Updating profile for user: {}", userDetails.getUsername());
        
        User user = userService.updateProfile(userDetails.getUsername(), updatedUser);
        return ResponseEntity.ok(user);
    }
    /**
 * GET ALL USERS (excluding current user)
 * URL: GET /api/users/all
 */
@GetMapping("/all")
public ResponseEntity<List<User>> getAllUsers(
        @AuthenticationPrincipal UserDetails userDetails) {
    
    log.info("Getting all users except: {}", userDetails.getUsername());
    
    List<User> allUsers = userService.getAllUsers();
    
    // Remove current user from list
    allUsers.removeIf(u -> u.getUsername().equals(userDetails.getUsername()));
    
    return ResponseEntity.ok(allUsers);
}
/**
 * UPLOAD PROFILE PICTURE
 */
@PostMapping("/profile/picture")
public ResponseEntity<?> uploadProfilePicture(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestParam("file") MultipartFile file) {
    
    try {
        String fileName = userService.saveProfilePicture(userDetails.getUsername(), file);
        return ResponseEntity.ok(Map.of("message", "Profile picture uploaded", "fileName", fileName));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
    }
}
}