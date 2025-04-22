package com.ecommerce.service;

import com.ecommerce.dto.StaffDTO;
import com.ecommerce.dto.StaffRegisterRequest;
import com.ecommerce.dto.AuthResponse;
import com.ecommerce.dto.UserDTO;
import com.ecommerce.entity.Staff;
import com.ecommerce.entity.User;
import com.ecommerce.entity.Role;
import com.ecommerce.entity.StaffPermission;
import com.ecommerce.repository.StaffRepository;
import com.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StaffService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    /**
     * Register a new staff member (only Admin can do this)
     */
    @Transactional
    public AuthResponse registerStaff(StaffRegisterRequest request, Long adminId) {
        // Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // Verify the admin exists
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        // Verify the admin is an actual admin
        if (admin.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only administrators can add staff members");
        }

        // Create staff user
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
        user.setRole(Role.STAFF);
        user.setVerified(true);

        User savedUser = userRepository.save(user);

        // Create staff permissions
        Staff staff = new Staff();
        staff.setUser(savedUser);
        staff.setPermissions(request.getPermissions());
        staff.setCreatedBy(adminId);
        staff.setCreatedAt(LocalDateTime.now());
        staff.setLastModified(LocalDateTime.now());

        // Staff savedStaff = staffRepository.save(staff);

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

    /**
     * Get all staff members (Admin only)
     */
    public List<StaffDTO> getAllStaff() {
        return staffRepository.findAll().stream()
                .map(this::convertToStaffDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get a specific staff member's details (Admin only)
     */
    public StaffDTO getStaffById(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        return convertToStaffDTO(staff);
    }

    /**
     * Update staff permissions (Admin only)
     */
    @Transactional
    public StaffDTO updateStaffPermissions(Long id, Set<StaffPermission> permissions, Long adminId) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        
        staff.setPermissions(permissions);
        staff.setLastModified(LocalDateTime.now());
        
        Staff updatedStaff = staffRepository.save(staff);
        return convertToStaffDTO(updatedStaff);
    }

    /**
     * Delete a staff member (Admin only)
     */
    @Transactional
    public void deleteStaff(Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        
        // Delete the staff record first due to foreign key constraint
        staffRepository.delete(staff);
        
        // Then delete the user
        userRepository.delete(staff.getUser());
    }

    /**
     * Check if current user has a specific permission
     */
    public boolean hasPermission(Long userId, StaffPermission permission) {
        Staff staff = staffRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        
        return staff.getPermissions().contains(permission);
    }

    /**
     * Helper method to convert Staff entity to StaffDTO
     */
    private StaffDTO convertToStaffDTO(Staff staff) {
        User user = staff.getUser();
        return new StaffDTO(
                staff.getId(),
                user.getId(),
                user.getEmail(),
                user.getName(),
                staff.getPermissions(),
                staff.getCreatedBy()
        );
    }
} 