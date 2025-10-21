package com.accenture.whatsapp.config;

import com.accenture.whatsapp.service.JwtService;
import com.accenture.whatsapp.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * FILTER: JwtAuthenticationFilter
 * Purpose: Validate JWT token on every request
 * 
 * FLOW FOR EVERY API REQUEST:
 * 1. Extract JWT token from Authorization header
 * 2. If token exists:
 *    - Extract username from token
 *    - Load user details from database
 *    - Validate token
 *    - If valid: Set authentication in Spring Security context
 * 3. Continue with request processing
 * 
 * This filter runs BEFORE the controller method is called
 */
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private UserService userService;
    
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        
        // Step 1: Extract Authorization header
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        
        // Check if Authorization header exists and starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // No token found, continue with filter chain
            // (Public endpoints will be accessible, protected ones will be denied)
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            // Step 2: Extract JWT token (remove "Bearer " prefix)
            jwt = authHeader.substring(7);
            
            // Step 3: Extract username from JWT token
            username = jwtService.extractUsername(jwt);
            
            // Step 4: If username exists and user is not already authenticated
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // Step 5: Load user details from database
                UserDetails userDetails = userService.loadUserByUsername(username);
                
                // Step 6: Validate JWT token
                if (jwtService.validateToken(jwt, userDetails)) {
                    
                    // Step 7: Create authentication token
                    UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,  // No credentials needed (already authenticated via JWT)
                            userDetails.getAuthorities()
                        );
                    
                    // Set additional details (IP address, session ID, etc.)
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // Step 8: Set authentication in Spring Security context
                    // This makes the user authenticated for this request
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    log.debug("JWT validated successfully for user: {}", username);
                }
            }
            
        } catch (Exception e) {
            // Log error but continue (will be caught by security config)
            log.error("JWT authentication error: {}", e.getMessage());
        }
        
        // Step 9: Continue with filter chain (proceed to controller)
        filterChain.doFilter(request, response);
    }
}