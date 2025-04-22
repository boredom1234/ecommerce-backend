package com.ecommerce.service;

import com.ecommerce.dto.AuthResponse;
import com.ecommerce.dto.UserDTO;
import com.ecommerce.entity.Role;
import com.ecommerce.entity.User;
import com.ecommerce.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class GoogleAuthService {

    private static final Logger logger = LoggerFactory.getLogger(GoogleAuthService.class);

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public AuthResponse authenticateWithGoogle(String tokenId) {
        try {
            GoogleIdToken idToken = verifyGoogleToken(tokenId);
            if (idToken == null) {
                throw new RuntimeException("Invalid Google token");
            }

            Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String googleUserId = payload.getSubject();
            String name = (String) payload.get("name");

            logger.info("Authenticated Google user: {}, {}", email, name);

            // Check if user exists by OAuth ID or email
            Optional<User> existingUser = userRepository.findByEmail(email);
            User user;

            if (existingUser.isPresent()) {
                user = existingUser.get();

                // If this is a regular user now trying to use OAuth, update their account
                if (user.getOauthProvider() == null) {
                    user.setOauthProvider("google");
                    user.setOauthProviderId(googleUserId);
                    user.setVerified(true);
                    userRepository.save(user);
                    logger.info("Updated existing user with Google OAuth: {}", email);
                }
            } else {
                // Create new user
                user = new User();
                user.setEmail(email);
                user.setName(name);
                // Generate a random secure password as they'll login via Google
                user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                user.setRole(Role.USER);
                user.setVerified(true);
                user.setOauthProvider("google");
                user.setOauthProviderId(googleUserId);
                user.setCreatedAt(LocalDateTime.now());

                userRepository.save(user);
                logger.info("Created new user from Google OAuth: {}", email);
            }

            // Update last login
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);

            // Generate JWT token
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));

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
                    user.getRole());

            return new AuthResponse(jwt, userDTO);
        } catch (GeneralSecurityException | IOException e) {
            logger.error("Error verifying Google token", e);
            throw new RuntimeException("Error authenticating with Google: " + e.getMessage());
        }
    }

    private GoogleIdToken verifyGoogleToken(String tokenId) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();
        return verifier.verify(tokenId);
    }
}