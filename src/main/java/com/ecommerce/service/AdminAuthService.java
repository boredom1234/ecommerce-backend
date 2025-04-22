package com.ecommerce.service;

import com.ecommerce.dto.AuthResponse;
import com.ecommerce.dto.LoginRequest;
import com.ecommerce.dto.RegisterRequest;
import com.ecommerce.dto.UserDTO;
import com.ecommerce.entity.User;
import com.ecommerce.entity.Role;
import com.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AdminAuthService {

    @Value("${admin.register.key}")
    private String adminRegistrationKey;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public AuthResponse registerAdmin(RegisterRequest request, String adminKey) {
        // Verify admin registration key
        if (!adminRegistrationKey.equals(adminKey)) {
            throw new RuntimeException("Invalid admin registration key");
        }

        // Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // Create admin user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setAddress(request.getAddress());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(Role.ADMIN);
        user.setVerified(true);

        User savedUser = userRepository.save(user);
        UserDTO userDTO = new UserDTO(
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

        // Generate JWT token
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));

        String jwt = jwtService.generateToken(userDetails);
        return new AuthResponse(jwt, userDTO);
    }

    public AuthResponse adminLogin(LoginRequest request) {

        // Verify user is an admin
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.ADMIN) {
            throw new RuntimeException("Unauthorized access - Admin only");
        }

        UserDTO userDTO = new UserDTO(user.getId(), user.getEmail(), user.getRole());

        // Generate JWT token
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));

        String jwt = jwtService.generateToken(userDetails);
        return new AuthResponse(jwt, userDTO);
    }
}