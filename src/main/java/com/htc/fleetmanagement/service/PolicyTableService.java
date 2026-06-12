package com.htc.fleetmanagement.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import com.htc.fleetmanagement.dto.PolicyTableRequest;
import com.htc.fleetmanagement.dto.PolicyTableResponse;

public interface PolicyTableService {
	PolicyTableResponse addNewPolicy(PolicyTableRequest request);
	
	public Optional<PolicyTableResponse> findById(String policyId);
	
	List<PolicyTableResponse> findAll();
	
	PolicyTableResponse updatePolicy(String policyId, PolicyTableRequest request);
	
	PolicyTableResponse partialUpdatePolicy(String policyId, PolicyTableRequest request);
	
	void deletePolicy(String policyId);
	
	byte[] downloadPoliciesAsExcel() throws IOException;
}

