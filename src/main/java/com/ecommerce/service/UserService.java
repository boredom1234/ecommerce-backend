package com.ecommerce.service;

import com.ecommerce.dto.UserDTO;
import com.ecommerce.dto.LoginRequest;
import com.ecommerce.dto.RegisterRequest;
import com.ecommerce.dto.OtpVerificationRequest;
import com.ecommerce.dto.UpdateProfileRequest;
import com.ecommerce.entity.User;
import com.ecommerce.entity.Role;
import com.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Random;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;
import com.ecommerce.dto.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    private String generateOTP() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    public void sendRegistrationOTP(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        String otp = generateOTP();
        User user = new User();
        user.setEmail(email);
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);

        // Send OTP via email
        String subject = "Email Verification OTP";
        String message = "Your OTP for email verification is: " + otp + "\nValid for 10 minutes.";
        emailService.sendEmail(email, subject, message);
    }

    public UserDTO verifyOTPAndRegister(OtpVerificationRequest request, String password) {
        return verifyOTPAndRegister(request, password, null, null, null, null, null, null, null);
    }

    public UserDTO verifyOTPAndRegister(OtpVerificationRequest request, String password,
                                    String name, String address, String phoneNumber) {
        return verifyOTPAndRegister(request, password, name, address, null, null, null, null, phoneNumber);
    }
    
    public UserDTO verifyOTPAndRegister(OtpVerificationRequest request, String password,
                                    String name, String address, String city, String state,
                                    String postalCode, String country, String phoneNumber) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!user.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }
        
        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired");
        }
        
        user.setPassword(passwordEncoder.encode(password));
        user.setVerified(true);
        
        if (name != null && !name.isEmpty()) {
            user.setName(name);
        }
        
        if (address != null && !address.isEmpty()) {
            user.setAddress(address);
        }
        
        if (city != null && !city.isEmpty()) {
            user.setCity(city);
        }
        
        if (state != null && !state.isEmpty()) {
            user.setState(state);
        }
        
        if (postalCode != null && !postalCode.isEmpty()) {
            user.setPostalCode(postalCode);
        }
        
        if (country != null && !country.isEmpty()) {
            user.setCountry(country);
        }
        
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            user.setPhoneNumber(phoneNumber);
        }
        
        user.setRole(Role.USER);
        user.setOtp(null);
        user.setOtpExpiry(null);
        
        User savedUser = userRepository.save(user);
        return new UserDTO(
            savedUser.getId(), 
            savedUser.getEmail(), 
            savedUser.getName(), 
            savedUser.getAddress(), 
            savedUser.getCity(),
            savedUser.getState(),
            savedUser.getPostalCode(),
            savedUser.getCountry(),
            savedUser.getPhoneNumber(), 
            savedUser.getRole()
        );
    }
    
    // New method to handle registration with additional fields
    public UserDTO registerUserWithDetails(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        
        // Create and save the user with all details
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setAddress(request.getAddress());
        user.setCity(request.getCity());
        user.setState(request.getState());
        user.setPostalCode(request.getPostalCode());
        user.setCountry(request.getCountry());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(Role.USER);
        user.setVerified(true); // Assuming no OTP verification when using this method
        
        User savedUser = userRepository.save(user);
        return new UserDTO(
            savedUser.getId(), 
            savedUser.getEmail(), 
            savedUser.getName(), 
            savedUser.getAddress(), 
            savedUser.getCity(),
            savedUser.getState(),
            savedUser.getPostalCode(),
            savedUser.getCountry(),
            savedUser.getPhoneNumber(), 
            savedUser.getRole()
        );
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }

    public void forgotPassword(String email) {
        logger.info("Processing forgot password request for email: {}", email);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found for email: {}", email);
                    return new RuntimeException("User not found");
                });

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(24));
        userRepository.save(user);
        
        logger.info("Generated reset token for user: {}", email);

        String resetLink = "Your Password Reset Token is: " + token;
        logger.info("Generated reset link: {}", resetLink);
        
        try {
            emailService.sendPasswordResetEmail(email, resetLink);
            logger.info("Password reset email sent successfully to: {}", email);
        } catch (Exception e) {
            logger.error("Failed to send password reset email: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
    }

    public UserDTO getCurrentUserDetails() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return new UserDTO(
            user.getId(), 
            user.getEmail(), 
            user.getName(), 
            user.getAddress(), 
            user.getCity(),
            user.getState(),
            user.getPostalCode(),
            user.getCountry(),
            user.getPhoneNumber(), 
            user.getRole()
        );
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
            user.getEmail(), user.getPassword(), 
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
        
        String jwt = jwtService.generateToken(userDetails);
        UserDTO userDTO = new UserDTO(
            user.getId(), 
            user.getEmail(), 
            user.getName(), 
            user.getAddress(), 
            user.getCity(),
            user.getState(),
            user.getPostalCode(),
            user.getCountry(),
            user.getPhoneNumber(), 
            user.getRole()
        );
        
        return new AuthResponse(jwt, userDTO);
    }

    public UserDTO updateUserProfile(UpdateProfileRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found for email: {}", email);
                    return new RuntimeException("User not found");
                });
        
        // Update basic profile information if provided
        if (request.getName() != null && !request.getName().isEmpty()) {
            user.setName(request.getName());
        }
        
        if (request.getAddress() != null && !request.getAddress().isEmpty()) {
            user.setAddress(request.getAddress());
        }
        
        if (request.getCity() != null && !request.getCity().isEmpty()) {
            user.setCity(request.getCity());
        }
        
        if (request.getState() != null && !request.getState().isEmpty()) {
            user.setState(request.getState());
        }
        
        if (request.getPostalCode() != null && !request.getPostalCode().isEmpty()) {
            user.setPostalCode(request.getPostalCode());
        }
        
        if (request.getCountry() != null && !request.getCountry().isEmpty()) {
            user.setCountry(request.getCountry());
        }
        
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        
        // Handle password change if requested
        if (request.getCurrentPassword() != null && request.getNewPassword() != null) {
            // Verify current password
            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                throw new RuntimeException("Current password is incorrect");
            }
            
            // Set new password
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            logger.info("Password updated for user: {}", email);
        }
        
        User updatedUser = userRepository.save(user);
        logger.info("Profile updated for user: {}", email);
        
        return new UserDTO(
            updatedUser.getId(), 
            updatedUser.getEmail(), 
            updatedUser.getName(), 
            updatedUser.getAddress(), 
            updatedUser.getCity(),
            updatedUser.getState(),
            updatedUser.getPostalCode(),
            updatedUser.getCountry(),
            updatedUser.getPhoneNumber(), 
            updatedUser.getRole()
        );
    }
} 