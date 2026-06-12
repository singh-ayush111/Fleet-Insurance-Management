package com.htc.fleetmanagement.controller;

import jakarta.validation.Valid;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.htc.fleetmanagement.dto.CorporateClientRequest;
import com.htc.fleetmanagement.dto.CorporateClientResponse;
import com.htc.fleetmanagement.exception.CorporateClientNotFoundException;
import com.htc.fleetmanagement.ratelimit.RateLimiter;
import com.htc.fleetmanagement.service.CorporateClientService;

@RestController
@RequestMapping("/api/corporate-clients")
@Validated
public class CorporateClientController {
	
	@Autowired
	private CorporateClientService corporateClientService;
	
	// Endpoint to retrieve a corporate client by ID
	@GetMapping("/{id}")
	public ResponseEntity<CorporateClientResponse> getClientById(@PathVariable Integer id) throws CorporateClientNotFoundException {
		CorporateClientResponse response = corporateClientService.findById(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	// Endpoint to register a new corporate client
	@PostMapping("/register")
	public ResponseEntity<CorporateClientResponse> registerClient(@Valid @RequestBody CorporateClientRequest request) {
		CorporateClientResponse response = corporateClientService.registerNewClient(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	// Endpoint to retrieve all corporate clients with rate limiting applied
	@RateLimiter(limit = 2, timeWindow = 5)
	@GetMapping
	public ResponseEntity<List<CorporateClientResponse>> getAllClients() {
		List<CorporateClientResponse> responses = corporateClientService.findAll();
		return new ResponseEntity<>(responses, HttpStatus.OK);
	}
	
	
	// Endpoint to update an existing corporate client by ID
	@PutMapping("/{id}")
	public ResponseEntity<CorporateClientResponse> updateClient(@PathVariable Integer id, @Valid @RequestBody CorporateClientRequest request) throws CorporateClientNotFoundException {
		CorporateClientResponse response = corporateClientService.updateClient(id, request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	
	// Endpoint to partially update an existing corporate client by ID
	@PatchMapping("/{id}")
	public ResponseEntity<CorporateClientResponse> partialUpdateClient(@PathVariable Integer id, @Valid @RequestBody CorporateClientRequest request) throws CorporateClientNotFoundException {
		CorporateClientResponse response = corporateClientService.partialUpdateClient(id, request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	// Endpoint to delete a corporate client by ID
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteClient(@PathVariable Integer id) throws CorporateClientNotFoundException {
		corporateClientService.deleteClient(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
