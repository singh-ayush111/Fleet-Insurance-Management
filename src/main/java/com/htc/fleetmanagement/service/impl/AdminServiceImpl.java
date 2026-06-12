package com.htc.fleetmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.htc.fleetmanagement.dto.AdminRequest;
import com.htc.fleetmanagement.dto.AdminResponse;
import com.htc.fleetmanagement.entity.Admin;
import com.htc.fleetmanagement.mapper.AdminMapper;
import com.htc.fleetmanagement.repository.AdminRepository;
import com.htc.fleetmanagement.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private AdminMapper adminMapper;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Override
    @Transactional
    public AdminResponse createAdmin(AdminRequest request) {
        Admin admin = adminMapper.toEntity(request);
        admin.setPassword(bCryptPasswordEncoder.encode(admin.getPassword()));
        Admin savedAdmin = adminRepository.save(admin);
        return adminMapper.toDto(savedAdmin);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<AdminResponse> findById(Integer id) {
        return adminRepository.findById(id).map(adminMapper::toDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AdminResponse> findAll() {
        List<Admin> admins = adminRepository.findAll();
        return admins.stream()
                .map(adminMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public AdminResponse updateAdmin(Integer id, AdminRequest request) {
        Admin admin = adminMapper.toEntity(request);
        admin.setUserId(id);
        Admin updatedAdmin = adminRepository.save(admin);
        return adminMapper.toDto(updatedAdmin);
    }
    
    @Override
    @Transactional
    public AdminResponse partialUpdateAdmin(Integer id, AdminRequest request) {
        Optional<Admin> existingAdmin = adminRepository.findById(id);
        if (existingAdmin.isPresent()) {
            Admin currentAdmin = existingAdmin.get();
            
            if (request.getUsername() != null && !request.getUsername().isBlank()) {
                currentAdmin.setUsername(request.getUsername());
            }
            if (request.getPassword() != null && !request.getPassword().isBlank()) {
                currentAdmin.setPassword(request.getPassword());
            }
            if (request.getRole() != null) {
                currentAdmin.setRole(request.getRole());
            }
            
            Admin updatedAdmin = adminRepository.save(currentAdmin);
            return adminMapper.toDto(updatedAdmin);
        }
        return null;
    }
    
    @Override
    @Transactional
    public void deleteAdmin(Integer id) {
        adminRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<AdminResponse> findByUsername(String username) {
        return adminRepository.findByUsername(username).map(adminMapper::toDto);
    }
    
}
