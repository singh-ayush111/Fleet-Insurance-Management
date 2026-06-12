package com.htc.fleetmanagement.controller;

import jakarta.validation.Valid;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.htc.fleetmanagement.dto.FleetVehicleRequest;
import com.htc.fleetmanagement.dto.FleetVehicleResponse;
import com.htc.fleetmanagement.service.FleetVehicleService;

@RestController
@RequestMapping("/api/fleet-vehicles")
@Validated
public class FleetVehicleController {
	
	@Autowired
	private FleetVehicleService fleetVehicleService;
	
	// Endpoint to add a new fleet vehicle
	@PostMapping("/add")
	public ResponseEntity<FleetVehicleResponse> addFleetVehicle(@Valid @RequestBody FleetVehicleRequest request) {
		FleetVehicleResponse response = fleetVehicleService.addFleetVehicle(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	// Endpoint to retrieve a fleet vehicle by ID
	@GetMapping("/{id}")
	public ResponseEntity<FleetVehicleResponse> getFleetVehicleById(@PathVariable Integer id) {
		return fleetVehicleService.findById(id)
				.map(response -> new ResponseEntity<>(response, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	
	// Endpoint to retrieve all fleet vehicles
	@GetMapping
	public ResponseEntity<List<FleetVehicleResponse>> getAllFleetVehicles() {
		List<FleetVehicleResponse> responses = fleetVehicleService.findAll();
		return new ResponseEntity<>(responses, HttpStatus.OK);
	}
	
	// Endpoint to update an existing fleet vehicle by ID
	@PutMapping("/{id}")
	public ResponseEntity<FleetVehicleResponse> updateFleetVehicle(@PathVariable Integer id, @Valid @RequestBody FleetVehicleRequest request) {
		return fleetVehicleService.findById(id)
				.map(existing -> {
					FleetVehicleResponse response = fleetVehicleService.updateFleetVehicle(id, request);
					return new ResponseEntity<>(response, HttpStatus.OK);
				})
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	// Endpoint to partially update an existing fleet vehicle by ID
	@PatchMapping("/{id}")
	public ResponseEntity<FleetVehicleResponse> partialUpdateFleetVehicle(@PathVariable Integer id, @Valid @RequestBody FleetVehicleRequest request) {
		return fleetVehicleService.findById(id)
				.map(existing -> {
					FleetVehicleResponse response = fleetVehicleService.partialUpdateFleetVehicle(id, request);
					return new ResponseEntity<>(response, HttpStatus.OK);
				})
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	// Endpoint to delete a fleet vehicle by ID
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteFleetVehicle(@PathVariable Integer id) {
		return fleetVehicleService.findById(id)
				.map(existing -> {
					fleetVehicleService.deleteFleetVehicle(id);
					return new ResponseEntity<Void>(HttpStatus.OK);
				})
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
}
