package com.htc.fleetmanagement.mapper;

import com.htc.fleetmanagement.dto.FleetManagerRequest;
import com.htc.fleetmanagement.dto.FleetManagerResponse;
import com.htc.fleetmanagement.entity.FleetManager;
import com.htc.fleetmanagement.entity.CorporateClient;
import org.springframework.stereotype.Component;

@Component
public class FleetManagerMapper {
    
    public FleetManagerResponse toDto(FleetManager manager) {
        if (manager == null) {
            return null;
        }
        
        FleetManagerResponse response = new FleetManagerResponse();
        response.setEmployeeId(manager.getEmployeeId());
        response.setName(manager.getName());
        response.setLicenseNumber(manager.getLicenseNumber());
        response.setCreatedAt(manager.getCreatedAt());
        response.setUpdatedAt(manager.getUpdatedAt());
        response.setEmail(manager.getEmail());
        response.setRole(manager.getRole());
        response.setUsername(manager.getUsername());
        

        
        if (manager.getClient() != null) {
            response.setClientId(manager.getClient().getUserId());
            response.setClientName(manager.getClient().getCompanyName());
        }
        
        return response;
    }
    
    public FleetManager toEntity(FleetManagerRequest request) {
        if (request == null) {
            return null;
        }
        
        FleetManager manager = new FleetManager();
        manager.setName(request.getName());
        manager.setLicenseNumber(request.getLicenseNumber());
        manager.setEmail(request.getEmail());
        manager.setPassword(request.getPassword());
        manager.setRole(request.getRole());
        manager.setUsername(request.getUsername());
        
        
        if (request.getClientId() != null) {
            CorporateClient client = new CorporateClient();
            client.setUserId(request.getClientId());
            manager.setClient(client);
        }
        
        return manager;
    }
}
