package com.accenture.whatsapp.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * SERVICE: JwtService
 * Purpose: Generate and validate JWT tokens
 * Flow:
 * 1. Generate token after successful login
 * 2. Validate token on every API request
 * 3. Extract username from token
 */
@Service
public class JwtService {
    
    // JWT secret key from application.properties
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    
    // Token expiration time from application.properties (24 hours)
    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;
    
    /**
     * Generate JWT token for a user
     * @param username - User's username
     * @return JWT token string
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }
    
    /**
     * Create JWT token with claims
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)  // Username
                .setIssuedAt(now)  // Token creation time
                .setExpiration(expiryDate)  // Token expiry time
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)  // Sign with secret key
                .compact();
    }
    
    /**
     * Get signing key for JWT
     */
    private Key getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    /**
     * Extract username from JWT token
     * @param token - JWT token
     * @return username
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    /**
     * Extract expiration date from token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    /**
     * Extract specific claim from token
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * Extract all claims from token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * Check if token is expired
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    /**
     * Validate JWT token
     * @param token - JWT token
     * @param userDetails - User details from database
     * @return true if valid, false otherwise
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}