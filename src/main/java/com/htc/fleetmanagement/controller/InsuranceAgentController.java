package com.htc.fleetmanagement.controller;

import jakarta.validation.Valid;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.htc.fleetmanagement.dto.ClaimStatusUpdateRequest;
import com.htc.fleetmanagement.dto.InsuranceAgentRequest;
import com.htc.fleetmanagement.dto.InsuranceAgentResponse;
import com.htc.fleetmanagement.dto.FleetClaimResponse;
import com.htc.fleetmanagement.exception.InsuranceAgentNotFoundException;
import com.htc.fleetmanagement.service.InsuranceAgentService;
import com.htc.fleetmanagement.service.FleetClaimService;

@RestController
@RequestMapping("/api/insurance-agents")
@Validated
public class InsuranceAgentController {
	
	@Autowired
	private InsuranceAgentService insuranceAgentService;
	
	@Autowired
	private FleetClaimService fleetClaimService;
	
	// Endpoints for insurance agent management (CRUD operations)
	@PostMapping("/register")
	public ResponseEntity<InsuranceAgentResponse> registerInsuranceAgent(@Valid @RequestBody InsuranceAgentRequest request) {
		InsuranceAgentResponse response = insuranceAgentService.registerInsuranceAgent(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	//
	@GetMapping("/{id}")
	public ResponseEntity<InsuranceAgentResponse> getInsuranceAgentById(@PathVariable Integer id) throws InsuranceAgentNotFoundException {
		InsuranceAgentResponse response = insuranceAgentService.findById(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<InsuranceAgentResponse>> getAllInsuranceAgents() {
		List<InsuranceAgentResponse> responses = insuranceAgentService.findAll();
		return new ResponseEntity<>(responses, HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<InsuranceAgentResponse> updateInsuranceAgent(@PathVariable Integer id, @Valid @RequestBody InsuranceAgentRequest request) throws InsuranceAgentNotFoundException {
		InsuranceAgentResponse response = insuranceAgentService.updateInsuranceAgent(id, request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<InsuranceAgentResponse> partialUpdateInsuranceAgent(@PathVariable Integer id, @Valid @RequestBody InsuranceAgentRequest request) throws InsuranceAgentNotFoundException {
		InsuranceAgentResponse response = insuranceAgentService.partialUpdateInsuranceAgent(id, request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteInsuranceAgent(@PathVariable Integer id) throws InsuranceAgentNotFoundException {
		insuranceAgentService.deleteInsuranceAgent(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	
	
	// CLAIM MANAGEMENT ENDPOINTS
	
	
	
	//* Get all claims for a specific client
	@GetMapping("/claims/client/{clientId}")
	@PreAuthorize("hasRole('INSURANCE_AGENT')")
	public ResponseEntity<List<FleetClaimResponse>> getClaimsByClient(@PathVariable Integer clientId) {
		List<FleetClaimResponse> claims = fleetClaimService.getClaimsByClientId(clientId);
		return new ResponseEntity<>(claims, HttpStatus.OK);
	}
	

	//Get all PENDING claims for a specific client
	@GetMapping("/claims/client/{clientId}/pending")
	@PreAuthorize("hasRole('INSURANCE_AGENT')")
	public ResponseEntity<List<FleetClaimResponse>> getPendingClaimsByClient(@PathVariable Integer clientId) {
		List<FleetClaimResponse> claims = fleetClaimService.getPendingClaimsByClientId(clientId);
		return new ResponseEntity<>(claims, HttpStatus.OK);
	}
	

	 //Update claim status from PENDING to APPROVED or REJECTED
	@PutMapping("/claims/{claimId}/status")
	@PreAuthorize("hasRole('INSURANCE_AGENT')")
	public ResponseEntity<FleetClaimResponse> updateClaimStatus(
			@PathVariable Integer claimId, 
			@Valid @RequestBody ClaimStatusUpdateRequest statusRequest) {
		FleetClaimResponse updatedClaim = fleetClaimService.updateClaimStatus(claimId, statusRequest.getStatus());
		return new ResponseEntity<>(updatedClaim, HttpStatus.OK);
	}
}

