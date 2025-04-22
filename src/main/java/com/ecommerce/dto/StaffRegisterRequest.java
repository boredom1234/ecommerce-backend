package com.ecommerce.dto;

import com.ecommerce.entity.StaffPermission;
import java.util.Set;

public class StaffRegisterRequest {
    private String email;
    private String password;
    private String name;
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String phoneNumber;
    private Set<StaffPermission> permissions;
    
    public StaffRegisterRequest() {
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
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
    
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public Set<StaffPermission> getPermissions() {
        return permissions;
    }
    
    public void setPermissions(Set<StaffPermission> permissions) {
        this.permissions = permissions;
    }
} 