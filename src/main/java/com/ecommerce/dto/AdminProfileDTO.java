package com.ecommerce.dto;

import com.ecommerce.entity.Role;
import java.time.LocalDateTime;

public class AdminProfileDTO {
    private Long id;
    private String email;
    private String name;
    private String address;
    private String phoneNumber;
    private Role role;
    private boolean verified;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    private int managedUsersCount;
    private int createdProductsCount;
    private int processedOrdersCount;
    
    // Default constructor
    public AdminProfileDTO() {
    }
    
    // Full constructor
    public AdminProfileDTO(Long id, String email, String name, String address, String phoneNumber, 
                           Role role, boolean verified, LocalDateTime lastLogin, LocalDateTime createdAt,
                           int managedUsersCount, int createdProductsCount, int processedOrdersCount) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.verified = verified;
        this.lastLogin = lastLogin;
        this.createdAt = createdAt;
        this.managedUsersCount = managedUsersCount;
        this.createdProductsCount = createdProductsCount;
        this.processedOrdersCount = processedOrdersCount;
    }
    
    // Basic user to AdminProfileDTO conversion constructor
    public AdminProfileDTO(UserDTO userDTO) {
        this.id = userDTO.getId();
        this.email = userDTO.getEmail();
        this.name = userDTO.getName();
        this.address = userDTO.getAddress();
        this.phoneNumber = userDTO.getPhoneNumber();
        this.role = userDTO.getRole();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
    
    public boolean isVerified() {
        return verified;
    }
    
    public void setVerified(boolean verified) {
        this.verified = verified;
    }
    
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public int getManagedUsersCount() {
        return managedUsersCount;
    }
    
    public void setManagedUsersCount(int managedUsersCount) {
        this.managedUsersCount = managedUsersCount;
    }
    
    public int getCreatedProductsCount() {
        return createdProductsCount;
    }
    
    public void setCreatedProductsCount(int createdProductsCount) {
        this.createdProductsCount = createdProductsCount;
    }
    
    public int getProcessedOrdersCount() {
        return processedOrdersCount;
    }
    
    public void setProcessedOrdersCount(int processedOrdersCount) {
        this.processedOrdersCount = processedOrdersCount;
    }
} 