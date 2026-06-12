package com.htc.fleetmanagement.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.htc.fleetmanagement.dto.AdminRequest;
import com.htc.fleetmanagement.dto.AdminResponse;
import com.htc.fleetmanagement.service.AdminService;
import com.htc.fleetmanagement.service.TokenCleanupService;
import com.htc.fleetmanagement.ratelimit.RateLimiter;

@RestController
@RequestMapping("/api/admins")
@Validated
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private TokenCleanupService tokenCleanupService;
    
    // Endpoint to create a new admin
    @PostMapping("/register")
    public ResponseEntity<AdminResponse> createAdmin(@Valid @RequestBody AdminRequest request) {
        AdminResponse response = adminService.createAdmin(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    // Endpoint to retrieve an admin by ID
    @GetMapping("/{id}")
    public ResponseEntity<AdminResponse> getAdminById(@PathVariable Integer id) {
        return adminService.findById(id)
                .map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    
    // Endpoint to retrieve all admins with rate limiting applied
    @RateLimiter(limit = 10, timeWindow = 60)
    @GetMapping
    public ResponseEntity<List<AdminResponse>> getAllAdmins() {
        List<AdminResponse> responses = adminService.findAll();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
    
    
    // Endpoint to update an existing admin by ID
    @PutMapping("/{id}")
    public ResponseEntity<AdminResponse> updateAdmin(@PathVariable Integer id, @Valid @RequestBody AdminRequest request) {
        return adminService.findById(id)
                .map(existing -> {
                    AdminResponse response = adminService.updateAdmin(id, request);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    
    // Endpoint to partially update an existing admin by ID
    @PatchMapping("/{id}")
    public ResponseEntity<AdminResponse> partialUpdateAdmin(@PathVariable Integer id, @Valid @RequestBody AdminRequest request) {
        return adminService.findById(id)
                .map(existing -> {
                    AdminResponse response = adminService.partialUpdateAdmin(id, request);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    
    // Endpoint to delete an existing admin by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Integer id) {
        return adminService.findById(id)
                .map(existing -> {
                    adminService.deleteAdmin(id);
                    return new ResponseEntity<Void>(HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    
    // Endpoint to retrieve an admin by username
    @GetMapping("/username/{username}")
    public ResponseEntity<AdminResponse> getAdminByUsername(@PathVariable String username) {
        return adminService.findByUsername(username)
                .map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    // Endpoint to trigger manual cleanup of expired and revoked tokens
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/cleanup/tokens")
    public ResponseEntity<Void> triggerTokenCleanup() {
        tokenCleanupService.triggerManualCleanup();
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
