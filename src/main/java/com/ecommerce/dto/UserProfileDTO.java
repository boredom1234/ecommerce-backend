package com.ecommerce.dto;

import com.ecommerce.entity.Role;
import java.time.LocalDateTime;

public class UserProfileDTO extends UserDTO {
    private Boolean verified;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private int orderCount;
    private double totalSpent;
    private LocalDateTime lastOrderDate;
    private Boolean accountLocked;
    private String accountStatus;
    
    // Default constructor
    public UserProfileDTO() {
        super();
    }
    
    // Constructor from UserDTO
    public UserProfileDTO(UserDTO userDTO) {
        super(userDTO.getId(), userDTO.getEmail(), userDTO.getName(), 
              userDTO.getAddress(), userDTO.getCity(), userDTO.getState(),
              userDTO.getPostalCode(), userDTO.getCountry(), userDTO.getPhoneNumber(), 
              userDTO.getRole());
    }
    
    // Full constructor
    public UserProfileDTO(Long id, String email, String name, String address, 
                         String city, String state, String postalCode, String country,
                         String phoneNumber, Role role, Boolean verified, LocalDateTime createdAt, 
                         LocalDateTime lastLogin, int orderCount, double totalSpent, 
                         LocalDateTime lastOrderDate, Boolean accountLocked, String accountStatus) {
        super(id, email, name, address, city, state, postalCode, country, phoneNumber, role);
        this.verified = verified;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
        this.orderCount = orderCount;
        this.totalSpent = totalSpent;
        this.lastOrderDate = lastOrderDate;
        this.accountLocked = accountLocked;
        this.accountStatus = accountStatus;
    }
    
    // Getters and Setters
    public boolean isVerified() {
        return verified != null ? verified : false;
    }
    
    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    public int getOrderCount() {
        return orderCount;
    }
    
    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }
    
    public double getTotalSpent() {
        return totalSpent;
    }
    
    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }
    
    public LocalDateTime getLastOrderDate() {
        return lastOrderDate;
    }
    
    public void setLastOrderDate(LocalDateTime lastOrderDate) {
        this.lastOrderDate = lastOrderDate;
    }
    
    public boolean isAccountLocked() {
        return accountLocked != null ? accountLocked : false;
    }
    
    public void setAccountLocked(Boolean accountLocked) {
        this.accountLocked = accountLocked;
    }
    
    public String getAccountStatus() {
        return accountStatus;
    }
    
    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
} 