package com.htc.fleetmanagement.mapper;

import com.htc.fleetmanagement.dto.CorporateClientRequest;
import com.htc.fleetmanagement.dto.CorporateClientResponse;
import com.htc.fleetmanagement.entity.CorporateClient;
import com.htc.fleetmanagement.entity.PolicyTable;
import com.htc.fleetmanagement.repository.PolicyTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CorporateClientMapper {
    
    @Autowired
    private PolicyTableRepository policyTableRepository;
    
    public CorporateClientResponse toDto(CorporateClient client) {
        if (client == null) {
            return null;
        }
        
        CorporateClientResponse response = new CorporateClientResponse();
        response.setUserId(client.getUserId());
        response.setUsername(client.getUsername());
        response.setRole(client.getRole());
        response.setCompanyName(client.getCompanyName());
        response.setContactEmail(client.getContactEmail());
        response.setCreatedAt(client.getCreatedAt());
        response.setUpdatedAt(client.getUpdatedAt());
        response.setVehicleCount(client.getVehicleCount());   
        response.setTotalPremium(client.getTotalPremium());   
        
        
        if (client.getMasterPolicy() != null) {
            response.setMasterPolicyNumber(client.getMasterPolicy().getMasterPolicyNumber());
        }
        
        return response;
    }
    
    public CorporateClient toEntity(CorporateClientRequest request) {
        if (request == null) {
            return null;
        }
        
        CorporateClient client = new CorporateClient();
        client.setUsername(request.getUsername());
        client.setPassword(request.getPassword());
        client.setCompanyName(request.getCompanyName());
        client.setContactEmail(request.getContactEmail());
        client.setRole(request.getRole());
        
        // Set the master policy by fetching from database using the master policy number
        if (request.getMasterPolicy() != null && request.getMasterPolicy().getMasterPolicyNumber() != null) {
            String masterPolicyNumber = request.getMasterPolicy().getMasterPolicyNumber();
            PolicyTable policyTable = policyTableRepository.findById(masterPolicyNumber).orElse(null);
            client.setMasterPolicy(policyTable);
        }
        
        return client;
    }
}

