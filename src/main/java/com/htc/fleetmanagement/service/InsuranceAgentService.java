package com.htc.fleetmanagement.service;

import java.util.List;

import com.htc.fleetmanagement.dto.InsuranceAgentRequest;
import com.htc.fleetmanagement.dto.InsuranceAgentResponse;
import com.htc.fleetmanagement.exception.InsuranceAgentNotFoundException;
import com.htc.fleetmanagement.util.AccountStatus;

public interface InsuranceAgentService {
	InsuranceAgentResponse registerInsuranceAgent(InsuranceAgentRequest request);
	
	InsuranceAgentResponse findById(Integer id) throws InsuranceAgentNotFoundException;
	
	List<InsuranceAgentResponse> findAll();
	
	InsuranceAgentResponse updateInsuranceAgent(Integer id, InsuranceAgentRequest request) throws InsuranceAgentNotFoundException;
	
	InsuranceAgentResponse partialUpdateInsuranceAgent(Integer id, InsuranceAgentRequest request) throws InsuranceAgentNotFoundException;
	
	void deleteInsuranceAgent(Integer id) throws InsuranceAgentNotFoundException;
	
	List<InsuranceAgentResponse> findByStatus(AccountStatus status);
	
	List<InsuranceAgentResponse> findByAgentId(Integer id);
}
