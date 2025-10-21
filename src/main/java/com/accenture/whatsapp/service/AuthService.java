package com.accenture.whatsapp.service;

import com.accenture.whatsapp.dto.AuthResponse;
import com.accenture.whatsapp.dto.LoginRequest;
import com.accenture.whatsapp.dto.RegisterRequest;
import com.accenture.whatsapp.entity.LoginAttempt;
import com.accenture.whatsapp.entity.User;
import com.accenture.whatsapp.repository.LoginAttemptRepository;
import com.accenture.whatsapp.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private LoginAttemptRepository loginAttemptRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registration attempt for username: {}", request.getUsername());
        
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already taken");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAbout("Hey there! I'm using WhatsApp Clone");
        user.setActive(true);
        user.setOnline(false);
        
        user = userRepository.save(user);
        log.info("User registered successfully: {}", user.getUsername());
        
        String token = jwtService.generateToken(user.getUsername());
        
        return new AuthResponse(
            token,
            "Bearer",
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getFullName(),
            "Registration successful"
        );
    }
    
    @Transactional
    public AuthResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        log.info("Login attempt for username: {}", request.getUsername());
        
        String ipAddress = getClientIp(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
                )
            );
            
            User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            
            user.setOnline(true);
            user.setLastSeen(LocalDateTime.now());
            userRepository.save(user);
            
            String token = jwtService.generateToken(user.getUsername());
            
            logLoginAttempt(user, request.getUsername(), true, ipAddress, userAgent, "Success");
            
            emailService.sendLoginSuccessEmail(user, ipAddress, userAgent);
            
            log.info("Login successful for user: {}", user.getUsername());
            
            return new AuthResponse(
                token,
                "Bearer",
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                "Login successful"
            );
            
        } catch (BadCredentialsException e) {
            log.warn("Login failed for username: {} - Bad credentials", request.getUsername());
            
            User user = userRepository.findByUsername(request.getUsername()).orElse(null);
            
            logLoginAttempt(user, request.getUsername(), false, ipAddress, userAgent, "Invalid password");
            
            if (user != null) {
                emailService.sendLoginFailureEmail(
                    user.getEmail(),
                    request.getUsername(),
                    "Invalid password",
                    ipAddress,
                    userAgent
                );
            }
            
            throw new RuntimeException("Invalid username or password");
            
        } catch (UsernameNotFoundException e) {
            log.warn("Login failed - User not found: {}", request.getUsername());
            
            logLoginAttempt(null, request.getUsername(), false, ipAddress, userAgent, "User not found");
            
            throw new RuntimeException("Invalid username or password");
        }
    }
    
    @Transactional
    public void logout(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        user.setOnline(false);
        user.setLastSeen(LocalDateTime.now());
        userRepository.save(user);
        
        log.info("User logged out: {}", username);
    }
    
    @Transactional
    public List<AuthResponse> registerMultiple(List<RegisterRequest> requests) {
        log.info("Starting bulk registration for {} users", requests.size());
        
        List<AuthResponse> responses = new ArrayList<>();
        
        for (RegisterRequest request : requests) {
            try {
                log.info("Registering user: {}", request.getUsername());
                
                if (userRepository.existsByUsername(request.getUsername())) {
                    log.warn("Username already taken: {}", request.getUsername());
                    responses.add(new AuthResponse(
                        null,
                        "Bearer",
                        null,
                        request.getUsername(),
                        request.getEmail(),
                        request.getFullName(),
                        "Failed: Username already taken"
                    ));
                    continue;
                }
                
                if (userRepository.existsByEmail(request.getEmail())) {
                    log.warn("Email already registered: {}", request.getEmail());
                    responses.add(new AuthResponse(
                        null,
                        "Bearer",
                        null,
                        request.getUsername(),
                        request.getEmail(),
                        request.getFullName(),
                        "Failed: Email already registered"
                    ));
                    continue;
                }
                
                User user = new User();
                user.setUsername(request.getUsername());
                user.setEmail(request.getEmail());
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setFullName(request.getFullName());
                user.setPhoneNumber(request.getPhoneNumber());
                user.setAbout("Hey there! I'm using WhatsApp Clone");
                user.setActive(true);
                user.setOnline(false);
                
                user = userRepository.save(user);
                log.info("User registered successfully: {}", user.getUsername());
                
                String token = jwtService.generateToken(user.getUsername());
                
                responses.add(new AuthResponse(
                    token,
                    "Bearer",
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getFullName(),
                    "Registration successful"
                ));
                
            } catch (Exception e) {
                log.error("Failed to register user {}: {}", request.getUsername(), e.getMessage());
                responses.add(new AuthResponse(
                    null,
                    "Bearer",
                    null,
                    request.getUsername(),
                    request.getEmail(),
                    request.getFullName(),
                    "Failed: " + e.getMessage()
                ));
            }
        }
        
        log.info("Bulk registration completed. Successfully registered {} out of {} users", 
                 responses.stream().filter(r -> r.getToken() != null).count(), 
                 requests.size());
        
        return responses;
    }
    
    private void logLoginAttempt(User user, String attemptedUsername, boolean successful, 
                                 String ipAddress, String userAgent, String failureReason) {
        LoginAttempt attempt = new LoginAttempt();
        attempt.setUser(user);
        attempt.setAttemptedUsername(attemptedUsername);
        attempt.setSuccessful(successful);
        attempt.setIpAddress(ipAddress);
        attempt.setUserAgent(userAgent);
        attempt.setFailureReason(successful ? null : failureReason);
        
        loginAttemptRepository.save(attempt);
        log.debug("Login attempt logged: {} - {}", attemptedUsername, successful ? "SUCCESS" : "FAILED");
    }
    
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}