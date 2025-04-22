package com.ecommerce.dto;

import com.ecommerce.entity.StaffPermission;
import java.util.Set;

public class StaffDTO {
    private Long id;
    private Long userId;
    private String email;
    private String name;
    private Set<StaffPermission> permissions;
    private Long createdBy;
    
    public StaffDTO() {
    }
    
    public StaffDTO(Long id, Long userId, String email, String name, Set<StaffPermission> permissions, Long createdBy) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.permissions = permissions;
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Set<StaffPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<StaffPermission> permissions) {
        this.permissions = permissions;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
} 