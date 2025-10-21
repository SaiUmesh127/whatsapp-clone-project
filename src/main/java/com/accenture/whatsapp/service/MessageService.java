package com.accenture.whatsapp.service;

import com.accenture.whatsapp.dto.MessageDto;
import com.accenture.whatsapp.entity.Message;
import com.accenture.whatsapp.entity.User;
import com.accenture.whatsapp.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SERVICE: MessageService
 * Purpose: Handle messaging operations
 */
@Service
@Slf4j
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private UserService userService;
    
    /**
     * SEND MESSAGE
     * Flow:
     * 1. Get sender and receiver from database
     * 2. Create message object
     * 3. Save to database
     * 4. Return message DTO
     */
    @Transactional
    public MessageDto sendMessage(String senderUsername, MessageDto messageDto) {
        log.info("Sending message from {} to user ID {}", senderUsername, messageDto.getReceiverId());
        
        // Get sender
        User sender = userService.getUserByUsername(senderUsername);
        
        // Get receiver
        User receiver = userService.getUserById(messageDto.getReceiverId());
        
        // Create message
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(messageDto.getContent());
        message.setMessageType(Message.MessageType.valueOf(messageDto.getMessageType()));
        message.setIsRead(false);
        
        // Save to database
        message = messageRepository.save(message);
        
        log.info("Message sent successfully: ID {}", message.getId());
        
        // Convert to DTO and return
        return convertToDto(message);
    }
    
    /**
     * GET CONVERSATION
     * Purpose: Get all messages between two users
     */
    public List<MessageDto> getConversation(String username, Long otherUserId) {
        User currentUser = userService.getUserByUsername(username);
        
        List<Message> messages = messageRepository.getConversation(currentUser.getId(), otherUserId);
        
        return messages.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * MARK MESSAGE AS READ
     */
    @Transactional
    public void markAsRead(Long messageId, String username) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        
        // Only receiver can mark as read
        if (!message.getReceiver().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized");
        }
        
        message.setIsRead(true);
        message.setReadAt(java.time.LocalDateTime.now());
        messageRepository.save(message);
    }
    
    /**
     * GET UNREAD MESSAGE COUNT
     */
    public long getUnreadCount(String username) {
        User user = userService.getUserByUsername(username);
        return messageRepository.countByReceiverAndIsReadFalse(user);
    }
    
    /**
     * ✨ ADD REACTION TO MESSAGE
     * Purpose: Add emoji reaction to a message
     * 
     * Flow:
     * 1. Find the message by ID
     * 2. Verify user is part of the conversation
     * 3. Validate reaction (only allowed emojis)
     * 4. Add or remove reaction (toggle behavior)
     * 5. Save and return updated message
     * 
     * @param messageId - ID of the message
     * @param reaction - Emoji reaction (e.g., "❤️", "👍", "😂")
     * @param username - Username of the person reacting
     */
    @Transactional
    public MessageDto addReaction(Long messageId, String reaction, String username) {
        log.info("Adding reaction '{}' to message {} by user {}", reaction, messageId, username);
        
        // Get the message
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found with ID: " + messageId));
        
        // Verify user is part of the conversation
        User currentUser = userService.getUserByUsername(username);
        boolean isParticipant = message.getSender().getId().equals(currentUser.getId()) 
                             || message.getReceiver().getId().equals(currentUser.getId());
        
        if (!isParticipant) {
            throw new RuntimeException("You can only react to messages in your conversations");
        }
        
        // Validate reaction (only allow specific emojis)
        List<String> allowedReactions = List.of("❤️", "👍", "😂", "😮", "😢", "🙏", "🔥", "👏");
        if (!allowedReactions.contains(reaction)) {
            throw new RuntimeException("Invalid reaction. Allowed reactions: " + String.join(" ", allowedReactions));
        }
        
        // Toggle reaction (if same reaction, remove it; otherwise set new reaction)
        if (reaction.equals(message.getReaction())) {
            // Remove reaction if user clicks same reaction again
            message.setReaction(null);
            log.info("Removed reaction from message {}", messageId);
        } else {
            // Set new reaction
            message.setReaction(reaction);
            log.info("Added reaction '{}' to message {}", reaction, messageId);
        }
        
        // Save to database
        message = messageRepository.save(message);
        
        // Convert to DTO and return
        return convertToDto(message);
    }
    
    /**
     * ✨ REMOVE REACTION FROM MESSAGE
     * Purpose: Remove reaction from a message
     * 
     * @param messageId - ID of the message
     * @param username - Username of the person removing reaction
     */
    @Transactional
    public MessageDto removeReaction(Long messageId, String username) {
        log.info("Removing reaction from message {} by user {}", messageId, username);
        
        // Get the message
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found with ID: " + messageId));
        
        // Verify user is part of the conversation
        User currentUser = userService.getUserByUsername(username);
        boolean isParticipant = message.getSender().getId().equals(currentUser.getId()) 
                             || message.getReceiver().getId().equals(currentUser.getId());
        
        if (!isParticipant) {
            throw new RuntimeException("You can only remove reactions from your conversations");
        }
        
        // Remove reaction
        message.setReaction(null);
        message = messageRepository.save(message);
        
        log.info("Removed reaction from message {}", messageId);
        
        // Convert to DTO and return
        return convertToDto(message);
    }
    
    /**
     * ✨ GET MESSAGES WITH SPECIFIC REACTION
     * Purpose: Get all messages with a specific reaction in user's conversations
     * 
     * @param username - Current user's username
     * @param reaction - Reaction to filter by
     */
    public List<MessageDto> getMessagesWithReaction(String username, String reaction) {
        log.info("Getting messages with reaction '{}' for user {}", reaction, username);
        
        User currentUser = userService.getUserByUsername(username);
        
        // Get all messages where user is sender or receiver
        List<Message> allMessages = messageRepository.findBySenderOrReceiver(currentUser, currentUser);
        
        // Filter by reaction
        List<Message> filteredMessages = allMessages.stream()
                .filter(msg -> reaction.equals(msg.getReaction()))
                .collect(Collectors.toList());
        
        log.info("Found {} messages with reaction '{}' for user {}", 
                filteredMessages.size(), reaction, username);
        
        // Convert to DTO
        return filteredMessages.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * CONVERT MESSAGE TO DTO
     * Purpose: Transform Message entity to MessageDto for API response
     * Includes reaction field
     */
    private MessageDto convertToDto(Message message) {
        MessageDto dto = new MessageDto();
        dto.setId(message.getId());
        dto.setSenderId(message.getSender().getId());
        dto.setSenderName(message.getSender().getFullName());
        dto.setReceiverId(message.getReceiver().getId());
        dto.setReceiverName(message.getReceiver().getFullName());
        dto.setContent(message.getContent());
        dto.setMessageType(message.getMessageType().toString());
        dto.setIsRead(message.getIsRead());
        dto.setSentAt(message.getSentAt().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")));
        dto.setReaction(message.getReaction()); // ✨ Includes reaction
        return dto;
    }
}