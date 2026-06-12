package com.htc.fleetmanagement.mapper;

import com.htc.fleetmanagement.dto.InsuranceAgentRequest;
import com.htc.fleetmanagement.dto.InsuranceAgentResponse;
import com.htc.fleetmanagement.entity.InsuranceAgent;
import org.springframework.stereotype.Component;

@Component
public class InsuranceAgentMapper {
    
    public InsuranceAgentResponse toDto(InsuranceAgent agent) {
        if (agent == null) {
            return null;
        }
        
        InsuranceAgentResponse response = new InsuranceAgentResponse();
        response.setUserId(agent.getUserId());
        response.setUsername(agent.getUsername());
        response.setRole(agent.getRole());
        response.setStatus(agent.getStatus());
        response.setCreatedAt(agent.getCreatedAt());
        response.setUpdatedAt(agent.getUpdatedAt());

        
        return response;
    }
    
    public InsuranceAgent toEntity(InsuranceAgentRequest request) {
        if (request == null) {
            return null;
        }
        
        InsuranceAgent agent = new InsuranceAgent();
        agent.setUsername(request.getUsername());
        agent.setPassword(request.getPassword());
        agent.setRole(request.getRole());
        agent.setStatus(request.getStatus());


        return agent;
    }
}
