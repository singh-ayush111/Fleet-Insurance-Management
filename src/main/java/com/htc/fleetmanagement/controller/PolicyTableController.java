package com.htc.fleetmanagement.controller;

import jakarta.validation.Valid;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.htc.fleetmanagement.dto.PolicyTableRequest;
import com.htc.fleetmanagement.dto.PolicyTableResponse;
import com.htc.fleetmanagement.service.PolicyTableService;

@RestController
@RequestMapping("/api/policies")
@Validated
public class PolicyTableController {
	
	@Autowired
	private PolicyTableService policyTableService;
	
	// Endpoint to add a new policy
	@PostMapping("/add")
	public ResponseEntity<PolicyTableResponse> addPolicy(@Valid @RequestBody PolicyTableRequest request) {
		PolicyTableResponse response = policyTableService.addNewPolicy(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	// Endpoint to retrieve a policy by ID
	@GetMapping("/{policyId}")
	public ResponseEntity<PolicyTableResponse> getPolicyById(@PathVariable String policyId) {
		return policyTableService.findById(policyId)
				.map(response -> new ResponseEntity<>(response, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	// Endpoint to retrieve all policies
	@GetMapping
	public ResponseEntity<List<PolicyTableResponse>> getAllPolicies() {
		List<PolicyTableResponse> responses = policyTableService.findAll();
		return new ResponseEntity<>(responses, HttpStatus.OK);
	}
	
	// Endpoint to update an existing policy by ID
	@PutMapping("/{policyId}")
	public ResponseEntity<PolicyTableResponse> updatePolicy(@PathVariable String policyId, @Valid @RequestBody PolicyTableRequest request) {
		return policyTableService.findById(policyId)
				.map(existing -> {
					PolicyTableResponse response = policyTableService.updatePolicy(policyId, request);
					return new ResponseEntity<>(response, HttpStatus.OK);
				})
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	// Endpoint to partially update an existing policy by ID
	@PatchMapping("/{policyId}")
	public ResponseEntity<PolicyTableResponse> partialUpdatePolicy(@PathVariable String policyId, @Valid @RequestBody PolicyTableRequest request) {
		return policyTableService.findById(policyId)
				.map(existing -> {
					PolicyTableResponse response = policyTableService.partialUpdatePolicy(policyId, request);
					return new ResponseEntity<>(response, HttpStatus.OK);
				})
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	// Endpoint to delete a policy by ID
	@DeleteMapping("/{policyId}")
	public ResponseEntity<Void> deletePolicy(@PathVariable String policyId) {
		return policyTableService.findById(policyId)
				.map(existing -> {
					policyTableService.deletePolicy(policyId);
					return new ResponseEntity<Void>(HttpStatus.OK);
				})
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	// Endpoint to download all policies as an Excel file
	@GetMapping("/download/excel")
	public ResponseEntity<byte[]> downloadPoliciesAsExcel() {
		try {
			byte[] excelContent = policyTableService.downloadPoliciesAsExcel();
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
			headers.add("Content-Disposition", "attachment; filename=policies_report.xlsx");
			headers.setContentLength(excelContent.length);
			
			return new ResponseEntity<>(excelContent, headers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
