package com.ecommerce.exception;

import com.ecommerce.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SecurityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(SecurityExceptionHandler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) 
            throws IOException, ServletException {
        logger.error("Authentication error: {}", exception.getMessage(), exception);
        
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        ApiResponse<?> apiResponse = new ApiResponse<>(false, "Authentication failed: " + exception.getMessage(), null);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) 
            throws IOException, ServletException {
        logger.error("Access denied: {}", exception.getMessage(), exception);
        
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        ApiResponse<?> apiResponse = new ApiResponse<>(false, "Access denied: " + exception.getMessage(), null);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
} 