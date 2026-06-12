package com.htc.fleetmanagement.mapper;

import com.htc.fleetmanagement.dto.AdminRequest;
import com.htc.fleetmanagement.dto.AdminResponse;
import com.htc.fleetmanagement.entity.Admin;
import org.springframework.stereotype.Component;

@Component
public class AdminMapper {
    
    public AdminResponse toDto(Admin admin) {
        if (admin == null) {
            return null;
        }
        
        AdminResponse response = new AdminResponse();
        response.setAdminId(admin.getUserId());
        response.setUsername(admin.getUsername());
        response.setRole(admin.getRole());
        response.setCreatedAt(admin.getCreatedAt());
        response.setUpdatedAt(admin.getUpdatedAt());
        
        return response;
    }
    
    public Admin toEntity(AdminRequest request) {
        if (request == null) {
            return null;
        }
        
        Admin admin = new Admin();
        admin.setUsername(request.getUsername());
        admin.setPassword(request.getPassword());
        admin.setRole(request.getRole());
        
        return admin;
    }
    
}
