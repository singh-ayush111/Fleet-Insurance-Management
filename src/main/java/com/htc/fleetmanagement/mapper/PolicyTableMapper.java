package com.htc.fleetmanagement.mapper;

import com.htc.fleetmanagement.dto.PolicyTableRequest;
import com.htc.fleetmanagement.dto.PolicyTableResponse;
import com.htc.fleetmanagement.entity.PolicyTable;
import org.springframework.stereotype.Component;

@Component
public class PolicyTableMapper {
    
    public PolicyTableResponse toDto(PolicyTable policy) {
        if (policy == null) {
            return null;
        }
        
        PolicyTableResponse response = new PolicyTableResponse();
        response.setMasterPolicyNumber(policy.getMasterPolicyNumber());
        response.setPolicyName(policy.getPolicyName());
        response.setPremium(policy.getPremium());
        response.setBenefits(policy.getBenefits());
        
        return response;
    }
    
    public PolicyTable toEntity(PolicyTableRequest request) {
        if (request == null) {
            return null;
        }
        
        PolicyTable policy = new PolicyTable();
        policy.setMasterPolicyNumber(request.getMasterPolicyNumber());
        policy.setPolicyName(request.getPolicyName());
        policy.setPremium(request.getPremium());
        policy.setBenefits(request.getBenefits());

        
        return policy;
    }
}
