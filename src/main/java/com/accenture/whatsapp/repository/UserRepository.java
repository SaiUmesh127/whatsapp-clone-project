package com.accenture.whatsapp.repository;

import com.accenture.whatsapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * REPOSITORY: UserRepository
 * Purpose: Data access layer for User entity
 * Extends JpaRepository for CRUD operations
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Find user by username (used for login)
    Optional<User> findByUsername(String username);
    
    // Find user by email (used for email notifications)
    Optional<User> findByEmail(String email);
    
    // Check if username already exists (used for registration validation)
    boolean existsByUsername(String username);
    
    // Check if email already exists (used for registration validation)
    boolean existsByEmail(String email);
    
    // Find all active users
    List<User> findByActiveTrue();
    
    // Find all online users
    List<User> findByOnlineTrue();
    
    // Search users by username or full name (for search functionality)
    @Query("SELECT u FROM User u WHERE u.username LIKE %:keyword% OR u.fullName LIKE %:keyword%")
    List<User> searchUsers(String keyword);
}