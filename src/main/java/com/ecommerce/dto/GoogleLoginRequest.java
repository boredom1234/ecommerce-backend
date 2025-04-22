package com.ecommerce.dto;

public class GoogleLoginRequest {
    private String tokenId;
    
    // Default constructor
    public GoogleLoginRequest() {
    }
    
    // Constructor with all fields
    public GoogleLoginRequest(String tokenId) {
        this.tokenId = tokenId;
    }
    
    // Getters and setters
    public String getTokenId() {
        return tokenId;
    }
    
    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
} 