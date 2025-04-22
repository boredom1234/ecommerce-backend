package com.ecommerce.controller;

import com.ecommerce.dto.UserDTO;
import com.ecommerce.dto.LoginRequest;
import com.ecommerce.dto.RegisterRequest;
import com.ecommerce.dto.OtpVerificationRequest;
import com.ecommerce.dto.UpdateProfileRequest;
import com.ecommerce.dto.AuthResponse;
import com.ecommerce.dto.ApiResponse;
import com.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        try {
            logger.info("Processing registration request for email: {}", request.getEmail());
            UserDTO userDTO = userService.registerUserWithDetails(request);
            return ResponseEntity.ok()
                    .body(new ApiResponse<>(true, "Registration successful", userDTO));
        } catch (RuntimeException e) {
            logger.error("Registration failed: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("Registration failed with unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An unexpected error occurred", null));
        }
    }

    @PostMapping("/register/send-otp")
    public ResponseEntity<?> sendRegistrationOTP(@RequestParam String email) {
        try {
            logger.info("Processing OTP request for email: {}", email);
            userService.sendRegistrationOTP(email);
            return ResponseEntity.ok()
                    .body(new ApiResponse<>(true, "OTP sent successfully", null));
        } catch (RuntimeException e) {
            logger.error("Failed to send OTP: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("Failed to send OTP with unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An unexpected error occurred", null));
        }
    }

    @PostMapping("/register/verify-otp")
    public ResponseEntity<?> verifyOTPAndRegister(
            @RequestBody OtpVerificationRequest request,
            @RequestParam String password,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String postalCode,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String phoneNumber) {
        try {
            logger.info("Processing OTP verification for email: {}", request.getEmail());
            UserDTO userDTO = userService.verifyOTPAndRegister(
                request, password, name, address, city, state, postalCode, country, phoneNumber
            );
            return ResponseEntity.ok()
                    .body(new ApiResponse<>(true, "Registration successful", userDTO));
        } catch (RuntimeException e) {
            logger.error("OTP verification failed: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("OTP verification failed with unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An unexpected error occurred", null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        try {
            userService.logout();
            return ResponseEntity.ok()
                    .body(new ApiResponse<>(true, "Logged out successfully", null));
        } catch (Exception e) {
            logger.error("Logout failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Logout failed: " + e.getMessage(), null));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            logger.info("Processing forgot password request for email: {}", email);
            userService.forgotPassword(email);
            return ResponseEntity.ok()
                    .body(new ApiResponse<>(true, "Password reset link sent to email", null));
        } catch (RuntimeException e) {
            logger.error("Forgot password failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("Forgot password failed with unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An unexpected error occurred", null));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {
        try {
            logger.info("Processing password reset request with token");
            userService.resetPassword(token, newPassword);
            return ResponseEntity.ok()
                    .body(new ApiResponse<>(true, "Password reset successfully", null));
        } catch (RuntimeException e) {
            logger.error("Password reset failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("Password reset failed with unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An unexpected error occurred", null));
        }
    }

    @GetMapping("/user-details")
    public ResponseEntity<?> getUserDetails() {
        try {
            UserDTO userDTO = userService.getCurrentUserDetails();
            return ResponseEntity.ok()
                    .body(new ApiResponse<>(true, "User details retrieved successfully", userDTO));
        } catch (RuntimeException e) {
            logger.error("Failed to retrieve user details: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("Failed to retrieve user details with unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An unexpected error occurred", null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            logger.info("Processing login request for email: {}", request.getEmail());
            AuthResponse response = userService.login(request);
            return ResponseEntity.ok()
                    .body(new ApiResponse<>(true, "User logged in successfully", response));
        } catch (RuntimeException e) {
            logger.error("Login failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Invalid email or password", null));
        } catch (Exception e) {
            logger.error("Login failed with unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An unexpected error occurred", null));
        }
    }

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateUserProfile(@RequestBody UpdateProfileRequest request) {
        try {
            logger.info("Processing profile update request");
            UserDTO userDTO = userService.updateUserProfile(request);
            return ResponseEntity.ok()
                    .body(new ApiResponse<>(true, "Profile updated successfully", userDTO));
        } catch (RuntimeException e) {
            logger.error("Profile update failed: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("Profile update failed with unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An unexpected error occurred", null));
        }
    }
} 