package com.ecommerce.controller;

import com.ecommerce.dto.ApiResponse;
import com.ecommerce.dto.AuthResponse;
import com.ecommerce.dto.StaffDTO;
import com.ecommerce.dto.StaffRegisterRequest;
import com.ecommerce.entity.StaffPermission;
import com.ecommerce.entity.User;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/admin/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;
    
    @Autowired
    private UserRepository userRepository;

    /**
     * Register a new staff member (Admin only)
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerStaff(@RequestBody StaffRegisterRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        // Get admin by email
        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        
        Long adminId = admin.getId();
        
        AuthResponse response = staffService.registerStaff(request, adminId);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Staff registered successfully", response));
    }

    /**
     * Get all staff members (Admin only)
     */
    @GetMapping
    public ResponseEntity<?> getAllStaff() {
        List<StaffDTO> staffList = staffService.getAllStaff();
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Staff retrieved successfully", staffList));
    }

    /**
     * Get a specific staff member's details (Admin only)
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getStaffById(@PathVariable Long id) {
        StaffDTO staff = staffService.getStaffById(id);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Staff retrieved successfully", staff));
    }

    /**
     * Update staff permissions (Admin only)
     */
    @PatchMapping("/{id}/permissions")
    public ResponseEntity<?> updateStaffPermissions(
            @PathVariable Long id,
            @RequestBody Set<StaffPermission> permissions) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        // Get admin by email
        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        
        Long adminId = admin.getId();
        
        StaffDTO updatedStaff = staffService.updateStaffPermissions(id, permissions, adminId);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Staff permissions updated successfully", updatedStaff));
    }

    /**
     * Delete a staff member (Admin only)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStaff(@PathVariable Long id) {
        staffService.deleteStaff(id);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Staff deleted successfully", null));
    }
} 