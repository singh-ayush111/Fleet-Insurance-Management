package com.htc.fleetmanagement.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.htc.fleetmanagement.dto.PolicyTableRequest;
import com.htc.fleetmanagement.dto.PolicyTableResponse;
import com.htc.fleetmanagement.entity.InsuranceAgent;
import com.htc.fleetmanagement.entity.PolicyTable;
import com.htc.fleetmanagement.entity.User;
import com.htc.fleetmanagement.exception.UnauthorizedAccessException;
import com.htc.fleetmanagement.mapper.PolicyTableMapper;
import com.htc.fleetmanagement.repository.InsuranceAgentRepository;
import com.htc.fleetmanagement.repository.PolicyTableRepository;
import com.htc.fleetmanagement.repository.UserRepository;
import com.htc.fleetmanagement.service.PolicyTableService;
import com.htc.fleetmanagement.util.AccountStatus;
import com.htc.fleetmanagement.util.ExcelReportUtil;


@Service
public class PolicyTableServiceImpl implements PolicyTableService{

	
	@Autowired
	private PolicyTableRepository policyrepo;
	
	@Autowired
	private PolicyTableMapper policyTableMapper;
	
	@Autowired
	private InsuranceAgentRepository insuranceAgentRepository;
	
	@Autowired
	private UserRepository userRepository;

	

	private void validateInsuranceAgentActive() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		Optional<User> userOptional = userRepository.findByUsername(username);
		if (!userOptional.isPresent()) {
			throw new UnauthorizedAccessException("User not found");
		}
		
		User user = userOptional.get();
		Optional<InsuranceAgent> agentOptional = insuranceAgentRepository.findById(user.getUserId());
		
		if (!agentOptional.isPresent()) {
			throw new UnauthorizedAccessException("Insurance Agent profile not found");
		}
		
		InsuranceAgent agent = agentOptional.get();
		
		if (agent.getStatus() != AccountStatus.ACTIVE) {
			throw new UnauthorizedAccessException("Insurance Agent account is not ACTIVE. Current status: " + agent.getStatus());
		}
	}
	
	@Override
	public PolicyTableResponse addNewPolicy(PolicyTableRequest request) {
		validateInsuranceAgentActive();
		PolicyTable policy = policyTableMapper.toEntity(request);
		PolicyTable savedPolicy = policyrepo.save(policy);
		return policyTableMapper.toDto(savedPolicy);
	}


	@Override
	public Optional<PolicyTableResponse> findById(String policyId) {
		validateInsuranceAgentActive();
		return policyrepo.findById(policyId).map(policyTableMapper::toDto);
	}

	@Override
	public List<PolicyTableResponse> findAll() {
		validateInsuranceAgentActive();
		List<PolicyTable> policies = policyrepo.findAll();
		return policies.stream()
				.map(policyTableMapper::toDto)
				.collect(Collectors.toList());
	}

	@Override
	public PolicyTableResponse updatePolicy(String policyId, PolicyTableRequest request) {
		validateInsuranceAgentActive();
		Optional<PolicyTable> existingPolicy = policyrepo.findById(policyId);
		if (existingPolicy.isPresent()) {
			PolicyTable policy = policyTableMapper.toEntity(request);
			policy.setMasterPolicyNumber(policyId);
			PolicyTable updatedPolicy = policyrepo.save(policy);
			return policyTableMapper.toDto(updatedPolicy);
		}
		return null;
	}

	@Override
	public PolicyTableResponse partialUpdatePolicy(String policyId, PolicyTableRequest request) {
		validateInsuranceAgentActive();
		Optional<PolicyTable> existingPolicy = policyrepo.findById(policyId);
		if (existingPolicy.isPresent()) {
			PolicyTable currentPolicy = existingPolicy.get();
			
			if (request.getPolicyName() != null) {
				currentPolicy.setPolicyName(request.getPolicyName());
			}
			if (request.getPremium() != null) {
				currentPolicy.setPremium(request.getPremium());
			}
			if (request.getBenefits() != null) {
				currentPolicy.setBenefits(request.getBenefits());
			}
			
			PolicyTable updatedPolicy = policyrepo.save(currentPolicy);
			return policyTableMapper.toDto(updatedPolicy);
		}
		return null;
	}

	@Override
	public void deletePolicy(String policyId) {
		validateInsuranceAgentActive();
		policyrepo.deleteById(policyId);
	}

	
	// generating the Excel report in-memory and returning it as a byte array.
	@Override
	public byte[] downloadPoliciesAsExcel() throws IOException {
		validateInsuranceAgentActive();
		List<PolicyTable> policies = policyrepo.findAll();
		return ExcelReportUtil.generatePoliciesExcelReport(policies);
	}
}
