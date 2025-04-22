package com.ecommerce.controller;

import com.ecommerce.dto.LoginRequest;
import com.ecommerce.dto.RegisterRequest;
import com.ecommerce.dto.AuthResponse;
import com.ecommerce.dto.ApiResponse;
import com.ecommerce.service.AdminAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminAuthController.class);

    @Autowired
    private AdminAuthService adminAuthService;

    @PostMapping("/register")
    public ResponseEntity<?> registerAdmin(
            @RequestBody RegisterRequest request,
            @RequestParam String adminKey) {
        try {
            logger.info("Attempting to register admin with email: {}", request.getEmail());
            AuthResponse response = adminAuthService.registerAdmin(request, adminKey);
            logger.info("Admin registration successful for email: {}", request.getEmail());
            return ResponseEntity.ok()
                    .body(new ApiResponse<>(true, "Admin registered successfully", response));
        } catch (RuntimeException e) {
            logger.error("Admin registration failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("Unexpected error during admin registration", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An unexpected error occurred", null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> adminLogin(@RequestBody LoginRequest request) {
        try {
            logger.info("Attempting admin login for email: {}", request.getEmail());
            AuthResponse response = adminAuthService.adminLogin(request);
            logger.info("Admin login successful for email: {}", request.getEmail());
            return ResponseEntity.ok()
                    .body(new ApiResponse<>(true, "Admin logged in successfully", response));
        } catch (RuntimeException e) {
            logger.error("Admin login failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("Unexpected error during admin login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An unexpected error occurred", null));
        }
    }
} 