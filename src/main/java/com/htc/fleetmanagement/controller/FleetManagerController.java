package com.htc.fleetmanagement.controller;

import jakarta.validation.Valid;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.htc.fleetmanagement.dto.FleetManagerRequest;
import com.htc.fleetmanagement.dto.FleetManagerResponse;
import com.htc.fleetmanagement.service.FleetManagerService;

@RestController
@RequestMapping("/api/fleet-managers")
@Validated
public class FleetManagerController {
	
	@Autowired
	private FleetManagerService fleetManagerService;
	
	// Endpoint to register a new fleet manager
	@PostMapping("/register")
	public ResponseEntity<FleetManagerResponse> registerFleetManager(@Valid @RequestBody FleetManagerRequest request) {
		FleetManagerResponse response = fleetManagerService.registerFleetManager(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	// Endpoint to retrieve a fleet manager by ID
	@GetMapping("/{id}")
	public ResponseEntity<FleetManagerResponse> getFleetManagerById(@PathVariable Integer id) {
		return fleetManagerService.findById(id)
				.map(response -> new ResponseEntity<>(response, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	// Endpoint to retrieve all fleet managers
	@GetMapping
	public ResponseEntity<List<FleetManagerResponse>> getAllFleetManagers() {
		List<FleetManagerResponse> responses = fleetManagerService.findAll();
		return new ResponseEntity<>(responses, HttpStatus.OK);
	}
	
	
	// Endpoint to update an existing fleet manager by ID
	@PutMapping("/{id}")
	public ResponseEntity<FleetManagerResponse> updateFleetManager(@PathVariable Integer id, @Valid @RequestBody FleetManagerRequest request) {
		return fleetManagerService.findById(id)
				.map(existing -> {
					FleetManagerResponse response = fleetManagerService.updateFleetManager(id, request);
					return new ResponseEntity<>(response, HttpStatus.OK);
				})
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	
	// Endpoint to partially update an existing fleet manager by ID
	@PatchMapping("/{id}")
	public ResponseEntity<FleetManagerResponse> partialUpdateFleetManager(@PathVariable Integer id, @Valid @RequestBody FleetManagerRequest request) {
		return fleetManagerService.findById(id)
				.map(existing -> {
					FleetManagerResponse response = fleetManagerService.partialUpdateFleetManager(id, request);
					return new ResponseEntity<>(response, HttpStatus.OK);
				})
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	
	// Endpoint to delete a fleet manager by ID
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteFleetManager(@PathVariable Integer id) {
		return fleetManagerService.findById(id)
				.map(existing -> {
					fleetManagerService.deleteFleetManager(id);
					return new ResponseEntity<Void>(HttpStatus.OK);
				})
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
}
