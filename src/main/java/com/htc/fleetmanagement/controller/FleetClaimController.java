package com.htc.fleetmanagement.controller;

import jakarta.validation.Valid;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.htc.fleetmanagement.dto.FleetClaimRequest;
import com.htc.fleetmanagement.dto.FleetClaimResponse;
import com.htc.fleetmanagement.service.FleetClaimService;

@RestController
@RequestMapping("/api/fleet-claims")
@Validated
public class FleetClaimController {
	
	@Autowired
	private FleetClaimService fleetClaimService;
	
	
	
	// End point for drivers to file claims
	@PostMapping("/file-claim")
	@PreAuthorize("hasRole('DRIVER')")
	public ResponseEntity<FleetClaimResponse> fileClaimAsDriver(@Valid @RequestBody FleetClaimRequest request) {
		FleetClaimResponse response = fleetClaimService.fileClaimAsDriver(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	
	// End point for admins to register claims on behalf of drivers
	@PostMapping("/register")
	public ResponseEntity<FleetClaimResponse> registerFleetClaim(@Valid @RequestBody FleetClaimRequest request) {
		FleetClaimResponse response = fleetClaimService.registerFleetClaim(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	// End point to retrieve a fleet claim by ID
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('DRIVER')")
	public ResponseEntity<FleetClaimResponse> getFleetClaimById(@PathVariable Integer id) {
		return fleetClaimService.findById(id)
				.map(response -> new ResponseEntity<>(response, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	// End point to retrieve all fleet claims with role-based access control
	@GetMapping
	@PreAuthorize("hasRole('DRIVER')")
	public ResponseEntity<List<FleetClaimResponse>> getAllFleetClaims() {
		List<FleetClaimResponse> responses = fleetClaimService.findAll();
		return new ResponseEntity<>(responses, HttpStatus.OK);
	}
	
	// End point to update an existing fleet claim by ID
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('DRIVER')")
	public ResponseEntity<FleetClaimResponse> updateFleetClaim(@PathVariable Integer id, @Valid @RequestBody FleetClaimRequest request) {
		return fleetClaimService.findById(id)
				.map(existing -> {
					FleetClaimResponse response = fleetClaimService.updateFleetClaim(id, request);
					return new ResponseEntity<>(response, HttpStatus.OK);
				})
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	// End point to partially update an existing fleet claim by ID
	@PatchMapping("/{id}")
	@PreAuthorize("hasRole('DRIVER')")
	public ResponseEntity<FleetClaimResponse> partialUpdateFleetClaim(@PathVariable Integer id, @Valid @RequestBody FleetClaimRequest request) {
		return fleetClaimService.findById(id)
				.map(existing -> {
					FleetClaimResponse response = fleetClaimService.partialUpdateFleetClaim(id, request);
					return new ResponseEntity<>(response, HttpStatus.OK);
				})
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	
	// End point to delete an existing fleet claim by ID
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('DRIVER')")
	public ResponseEntity<Void> deleteFleetClaim(@PathVariable Integer id) {
		return fleetClaimService.findById(id)
				.map(existing -> {
					fleetClaimService.deleteFleetClaim(id);
					return new ResponseEntity<Void>(HttpStatus.OK);
				})
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
}