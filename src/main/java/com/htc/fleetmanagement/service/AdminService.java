package com.htc.fleetmanagement.service;

import java.util.List;
import java.util.Optional;
import com.htc.fleetmanagement.dto.AdminRequest;
import com.htc.fleetmanagement.dto.AdminResponse;

public interface AdminService {
    
    AdminResponse createAdmin(AdminRequest request);
    
    Optional<AdminResponse> findById(Integer id);
    
    List<AdminResponse> findAll();
    
    AdminResponse updateAdmin(Integer id, AdminRequest request);
    
    AdminResponse partialUpdateAdmin(Integer id, AdminRequest request);
    
    void deleteAdmin(Integer id);
    
    Optional<AdminResponse> findByUsername(String username);
    
}
