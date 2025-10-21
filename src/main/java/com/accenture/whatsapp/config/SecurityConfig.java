package com.accenture.whatsapp.config;

import com.accenture.whatsapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

/**
 * CONFIGURATION: SecurityConfig
 * Purpose: Configure Spring Security
 * 
 * SECURITY FLOW:
 * 1. All requests go through security filter chain
 * 2. JWT filter validates token (except login/register)
 * 3. If token valid → Allow request
 * 4. If token invalid → Return 401 Unauthorized
 * 5. Passwords are encrypted using BCrypt
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    /**
     * PASSWORD ENCODER
     * Purpose: Encrypt passwords using BCrypt algorithm
     * BCrypt is a strong one-way hash function
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * AUTHENTICATION PROVIDER
     * Purpose: Configure how users are authenticated
     * Uses UserService to load user details and BCrypt to verify password
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);  // Load user from database
        authProvider.setPasswordEncoder(passwordEncoder());  // Verify password
        return authProvider;
    }
    
    /**
     * AUTHENTICATION MANAGER
     * Purpose: Central point for authentication
     * Used in AuthService during login
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    /**
     * SECURITY FILTER CHAIN
     * Purpose: Define which endpoints are protected and which are public
     * 
     * PUBLIC ENDPOINTS (No authentication required):
     * - POST /api/auth/register
     * - POST /api/auth/login
     * 
     * PROTECTED ENDPOINTS (JWT token required):
     * - All other /api/** endpoints
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF (not needed for REST APIs with JWT)
            .csrf(csrf -> csrf.disable())
            
            // Enable CORS (Cross-Origin Resource Sharing) for Angular frontend
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // Configure authorization rules
            .authorizeHttpRequests(auth -> auth
            // Public endpoints - Anyone can access
            .requestMatchers(
                "/api/auth/register", 
                "/api/auth/login", 
                "/api/auth/register-multiple"  // ✨ ADD THIS LINE
            ).permitAll()
    
    // All other endpoints require authentication
    .anyRequest().authenticated()
)
            
            // Stateless session management (no server-side sessions)
            // Each request must include JWT token
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Set authentication provider
            .authenticationProvider(authenticationProvider())
            
            // Add JWT filter before Spring Security's authentication filter
            // This filter will validate JWT token on every request
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    /**
     * CORS CONFIGURATION
     * Purpose: Allow Angular frontend to make requests to this API
     * Configure which origins, methods, and headers are allowed
     */
    /**
 * CORS CONFIGURATION
 * Purpose: Allow Angular frontend to make requests to this API
 */
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    
    // Allow requests from Angular development server
    configuration.setAllowedOrigins(Arrays.asList(
        "http://localhost:4200",    // Angular default port
        "http://localhost:3000",    // Alternative port
        "http://127.0.0.1:4200"
    ));
    
    // Allow all HTTP methods
    configuration.setAllowedMethods(Arrays.asList(
        "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
    ));
    
    // Allow all headers
    configuration.setAllowedHeaders(Arrays.asList("*"));
    
    // Allow credentials (cookies, authorization headers)
    configuration.setAllowCredentials(true);
    
    // Expose Authorization header to frontend
    configuration.setExposedHeaders(Arrays.asList("Authorization"));
    
    // Cache preflight requests for 1 hour
    configuration.setMaxAge(3600L);
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    
    return source;
}
}