package com.accenture.whatsapp.controller;

import com.accenture.whatsapp.dto.MessageDto;
import com.accenture.whatsapp.service.MessageService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLLER: MessageController
 * Purpose: Handle messaging operations
 * Base URL: /api/messages
 * All endpoints require JWT authentication
 * 
 * ENDPOINTS:
 * POST /api/messages - Send message
 * GET /api/messages/conversation/{userId} - Get conversation with user
 * PUT /api/messages/{id}/read - Mark message as read
 * GET /api/messages/unread-count - Get unread message count
 * PUT /api/messages/{id}/react - Add/Remove reaction to message
 * DELETE /api/messages/{id}/react - Remove reaction from message
 * GET /api/messages/reactions/{reaction} - Get messages with specific reaction
 */
@RestController
@RequestMapping("/api/messages")
@Slf4j
public class MessageController {
    
    @Autowired
    private MessageService messageService;
    
    /**
     * SEND MESSAGE
     * URL: POST /api/messages
     * Headers: Authorization: Bearer <token>
     * Request Body: MessageDto (JSON)
     * 
     * Example Request:
     * {
     *   "receiverId": 2,
     *   "content": "Hello! How are you?",
     *   "messageType": "TEXT"
     * }
     */
    @PostMapping
    public ResponseEntity<MessageDto> sendMessage(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody MessageDto messageDto) {
        
        log.info("Sending message from {} to user {}", 
                userDetails.getUsername(), messageDto.getReceiverId());
        
        MessageDto sentMessage = messageService.sendMessage(
                userDetails.getUsername(), messageDto);
        
        return ResponseEntity.ok(sentMessage);
    }
    
    /**
     * GET CONVERSATION
     * URL: GET /api/messages/conversation/{userId}
     * Returns all messages between current user and specified user
     */
    @GetMapping("/conversation/{userId}")
    public ResponseEntity<List<MessageDto>> getConversation(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long userId) {
        
        log.info("Getting conversation between {} and user {}", 
                userDetails.getUsername(), userId);
        
        List<MessageDto> messages = messageService.getConversation(
                userDetails.getUsername(), userId);
        
        return ResponseEntity.ok(messages);
    }
    
    /**
     * MARK MESSAGE AS READ
     * URL: PUT /api/messages/{id}/read
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        
        log.info("Marking message {} as read by {}", id, userDetails.getUsername());
        
        messageService.markAsRead(id, userDetails.getUsername());
        
        return ResponseEntity.ok().build();
    }
    
    /**
     * GET UNREAD MESSAGE COUNT
     * URL: GET /api/messages/unread-count
     */
    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        long count = messageService.getUnreadCount(userDetails.getUsername());
        
        return ResponseEntity.ok(count);
    }
    
    /**
     * ✨ ADD REACTION TO MESSAGE
     * URL: PUT /api/messages/{id}/react
     * Headers: Authorization: Bearer <token>
     * Request Param: reaction (emoji)
     * 
     * Example: PUT /api/messages/123/react?reaction=❤️
     * 
     * Allowed Reactions: ❤️ 👍 😂 😮 😢 🙏 🔥 👏
     */
    @PutMapping("/{id}/react")
    public ResponseEntity<MessageDto> addReaction(
            @PathVariable Long id,
            @RequestParam String reaction,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("Adding reaction '{}' to message {} by user {}", 
                reaction, id, userDetails.getUsername());
        
        try {
            MessageDto updatedMessage = messageService.addReaction(
                    id, reaction, userDetails.getUsername());
            
            return ResponseEntity.ok(updatedMessage);
            
        } catch (Exception e) {
            log.error("Error adding reaction: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * ✨ REMOVE REACTION FROM MESSAGE
     * URL: DELETE /api/messages/{id}/react
     * Headers: Authorization: Bearer <token>
     * 
     * Example: DELETE /api/messages/123/react
     */
    @DeleteMapping("/{id}/react")
    public ResponseEntity<MessageDto> removeReaction(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("Removing reaction from message {} by user {}", 
                id, userDetails.getUsername());
        
        try {
            MessageDto updatedMessage = messageService.removeReaction(
                    id, userDetails.getUsername());
            
            return ResponseEntity.ok(updatedMessage);
            
        } catch (Exception e) {
            log.error("Error removing reaction: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * ✨ GET MESSAGES WITH SPECIFIC REACTION
     * URL: GET /api/messages/reactions/{reaction}
     * Headers: Authorization: Bearer <token>
     * 
     * Example: GET /api/messages/reactions/❤️
     * 
     * Returns all messages in your conversations that have the specified reaction
     */
    @GetMapping("/reactions/{reaction}")
    public ResponseEntity<List<MessageDto>> getMessagesWithReaction(
            @PathVariable String reaction,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("Getting messages with reaction '{}' for user {}", 
                reaction, userDetails.getUsername());
        
        try {
            List<MessageDto> messages = messageService.getMessagesWithReaction(
                    userDetails.getUsername(), reaction);
            
            return ResponseEntity.ok(messages);
            
        } catch (Exception e) {
            log.error("Error getting messages with reaction: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}