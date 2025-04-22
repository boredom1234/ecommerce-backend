package com.ecommerce.controller;

import com.ecommerce.dto.AdminProductDTO;
import com.ecommerce.dto.OrderDTO;
import com.ecommerce.dto.UserDTO;
import com.ecommerce.dto.ApiResponse;
import com.ecommerce.dto.AdminProfileDTO;
import com.ecommerce.dto.UserProfileDTO;
import com.ecommerce.dto.AdminStatisticsDTO;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.Role;
import com.ecommerce.service.AdminService;
import com.ecommerce.service.AdminService.ProductStockUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // Product Management
    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestBody AdminProductDTO productDTO) {
        AdminProductDTO savedProduct = adminService.addProduct(productDTO);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Product added successfully", savedProduct));
    }

    @PatchMapping("/product/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @RequestBody AdminProductDTO productDTO) {
        AdminProductDTO updatedProduct = adminService.updateProduct(id, productDTO);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Product updated successfully", updatedProduct));
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        adminService.deleteProduct(id);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Product deleted successfully", null));
    }

    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<AdminProductDTO> products = adminService.getAllProducts(PageRequest.of(page, size));
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Products retrieved successfully", products));
    }
    
    // Stock Management
    @PatchMapping("/product/{id}/stock")
    public ResponseEntity<?> updateProductStock(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        AdminProductDTO updatedProduct = adminService.updateProductStock(id, quantity);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Stock updated successfully", updatedProduct));
    }
    
    @GetMapping("/products/low-stock")
    public ResponseEntity<?> getLowStockProducts(
            @RequestParam(defaultValue = "10") Integer threshold) {
        List<AdminProductDTO> products = adminService.getLowStockProducts(threshold);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Low stock products retrieved successfully", products));
    }
    
    @PostMapping("/products/bulk-stock-update")
    public ResponseEntity<?> bulkUpdateStock(
            @RequestBody List<ProductStockUpdateRequest> updates) {
        List<AdminProductDTO> updatedProducts = adminService.bulkUpdateStock(updates);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Bulk stock update successful", updatedProducts));
    }

    // Order Management
    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrders() {
        List<OrderDTO> orders = adminService.getAllOrders();
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Orders retrieved successfully", orders));
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<?> getOrder(@PathVariable Long id) {
        OrderDTO order = adminService.getOrder(id);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Order retrieved successfully", order));
    }

    @PatchMapping("/order/{id}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam Order.OrderStatus status) {
        OrderDTO updatedOrder = adminService.updateOrderStatus(id, status);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Order status updated successfully", updatedOrder));
    }

    @DeleteMapping("/order/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        adminService.deleteOrder(id);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Order deleted successfully", null));
    }

    // Basic User Management
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(defaultValue = "0", required = false) Integer page,
            @RequestParam(defaultValue = "20", required = false) Integer size) {
        if (page != null && size != null) {
            Page<UserDTO> users = adminService.getUsersWithPagination(PageRequest.of(page, size));
            return ResponseEntity.ok()
                    .body(new ApiResponse<>(true, "Users retrieved successfully", users));
        } else {
            List<UserDTO> users = adminService.getAllUsers();
            return ResponseEntity.ok()
                    .body(new ApiResponse<>(true, "Users retrieved successfully", users));
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        UserDTO user = adminService.getUser(id);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "User retrieved successfully", user));
    }

    @PatchMapping("/user/{id}/role")
    public ResponseEntity<?> updateUserRole(
            @PathVariable Long id,
            @RequestParam Role role) {
        UserDTO updatedUser = adminService.updateUserRole(id, role);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "User role updated successfully", updatedUser));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "User deleted successfully", null));
    }
    
    // Advanced User and Admin Profile Management
    
    @GetMapping("/users/search")
    public ResponseEntity<?> searchUsers(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Role role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        if (email != null && !email.isEmpty()) {
            List<UserDTO> users = adminService.searchUsersByEmail(email);
            return ResponseEntity.ok()
                    .body(new ApiResponse<>(true, "Users retrieved successfully", users));
        } else if (name != null && !name.isEmpty()) {
            List<UserDTO> users = adminService.searchUsersByName(name);
            return ResponseEntity.ok()
                    .body(new ApiResponse<>(true, "Users retrieved successfully", users));
        } else if (role != null) {
            Page<UserDTO> users = adminService.getUsersByRole(role, PageRequest.of(page, size));
            return ResponseEntity.ok()
                    .body(new ApiResponse<>(true, "Users retrieved successfully", users));
        } else {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "At least one search parameter is required", null));
        }
    }
    
    @GetMapping("/user-profile/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long id) {
        UserProfileDTO profile = adminService.getUserProfile(id);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "User profile retrieved successfully", profile));
    }
    
    @GetMapping("/users/profiles")
    public ResponseEntity<?> getAllUserProfiles() {
        List<UserProfileDTO> profiles = adminService.getAllUserProfiles();
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "User profiles retrieved successfully", profiles));
    }
    
    @GetMapping("/admin-profile/{id}")
    public ResponseEntity<?> getAdminProfile(@PathVariable Long id) {
        AdminProfileDTO profile = adminService.getAdminProfile(id);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Admin profile retrieved successfully", profile));
    }
    
    @GetMapping("/admin-profiles")
    public ResponseEntity<?> getAllAdminProfiles() {
        List<AdminProfileDTO> profiles = adminService.getAllAdminProfiles();
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Admin profiles retrieved successfully", profiles));
    }
    
    @GetMapping("/current-admin-profile")
    public ResponseEntity<?> getCurrentAdminProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        // Get the user ID from email
        UserDTO user = adminService.getAllUsers().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        
        AdminProfileDTO profile = adminService.getAdminProfile(user.getId());
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Current admin profile retrieved successfully", profile));
    }
    
    @PatchMapping("/user-profile/{id}")
    public ResponseEntity<?> updateUserProfile(
            @PathVariable Long id,
            @RequestBody UserProfileDTO profileDTO) {
        UserProfileDTO updatedProfile = adminService.updateUserProfile(id, profileDTO);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "User profile updated successfully", updatedProfile));
    }
    
    @PatchMapping("/admin-profile/{id}")
    public ResponseEntity<?> updateAdminProfile(
            @PathVariable Long id,
            @RequestBody AdminProfileDTO profileDTO) {
        AdminProfileDTO updatedProfile = adminService.updateAdminProfile(id, profileDTO);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Admin profile updated successfully", updatedProfile));
    }
    
    @PatchMapping("/user/{id}/toggle-lock")
    public ResponseEntity<?> toggleUserAccountLock(@PathVariable Long id) {
        adminService.toggleUserAccountLock(id);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "User account lock status toggled successfully", null));
    }
    
    @PostMapping("/user/{id}/reset-password")
    public ResponseEntity<?> resetUserPassword(
            @PathVariable Long id,
            @RequestParam String newPassword) {
        adminService.resetUserPassword(id, newPassword);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "User password reset successfully", null));
    }
    
    @GetMapping("/users/statistics")
    public ResponseEntity<?> getUsersStatistics() {
        Map<String, Long> stats = adminService.getUsersStatistics();
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "User statistics retrieved successfully", stats));
    }
    
    @GetMapping("/dashboard/statistics")
    public ResponseEntity<?> getDashboardStatistics() {
        AdminStatisticsDTO stats = adminService.getAdminDashboardStatistics();
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Dashboard statistics retrieved successfully", stats));
    }
} 