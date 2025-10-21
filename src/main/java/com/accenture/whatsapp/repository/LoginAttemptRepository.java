package com.accenture.whatsapp.repository;

import com.accenture.whatsapp.entity.LoginAttempt;
import com.accenture.whatsapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REPOSITORY: LoginAttemptRepository
 * Purpose: Track and query login attempts
 */
@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {
    
    // Get all login attempts for a user
    List<LoginAttempt> findByUserOrderByAttemptedAtDesc(User user);
    
    // Get recent failed login attempts (for security - block after X failed attempts)
    List<LoginAttempt> findByAttemptedUsernameAndSuccessfulFalseAndAttemptedAtAfter(
        String username, LocalDateTime after);
    
    // Get last N attempts for a user
    List<LoginAttempt> findTop10ByUserOrderByAttemptedAtDesc(User user);
}