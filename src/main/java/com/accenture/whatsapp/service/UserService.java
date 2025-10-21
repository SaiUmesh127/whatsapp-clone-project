package com.accenture.whatsapp.service;

import com.accenture.whatsapp.entity.User;
import com.accenture.whatsapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * SERVICE: UserService
 * Purpose: User-related operations
 * Implements UserDetailsService for Spring Security
 */
@Service
@Slf4j
public class UserService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * LOAD USER BY USERNAME
     * Purpose: Used by Spring Security for authentication
     * This method is called during login
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username: {}", username);
        
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        
        // Return Spring Security UserDetails object
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(new ArrayList<>())  // No roles for simplicity
                .accountExpired(false)
                .accountLocked(!user.getActive())
                .credentialsExpired(false)
                .disabled(!user.getActive())
                .build();
    }
    
    /**
     * GET USER BY ID
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    /**
     * GET USER BY USERNAME
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
    
    /**
     * SEARCH USERS
     */
    public List<User> searchUsers(String keyword) {
        return userRepository.searchUsers(keyword);
    }
    
    /**
     * GET ALL ONLINE USERS
     */
    public List<User> getOnlineUsers() {
        return userRepository.findByOnlineTrue();
    }
    
    /**
     * UPDATE USER PROFILE
     */
    public User updateProfile(String username, User updatedUser) {
        User user = getUserByUsername(username);
        
        if (updatedUser.getFullName() != null) {
            user.setFullName(updatedUser.getFullName());
        }
        if (updatedUser.getAbout() != null) {
            user.setAbout(updatedUser.getAbout());
        }
        if (updatedUser.getProfilePicture() != null) {
            user.setProfilePicture(updatedUser.getProfilePicture());
        }
        if (updatedUser.getPhoneNumber() != null) {
            user.setPhoneNumber(updatedUser.getPhoneNumber());
        }
        
        return userRepository.save(user);
    }
    public List<User> getAllUsers() {
    return userRepository.findByActiveTrue();
}
@Scheduled(fixedRate = 5000) // Every 5 seconds
public void updateLastSeen() {
    // Update last seen for all online users
}

public String saveProfilePicture(String username, MultipartFile file) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'saveProfilePicture'");
}
}