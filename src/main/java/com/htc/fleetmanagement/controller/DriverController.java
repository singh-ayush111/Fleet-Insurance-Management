package com.htc.fleetmanagement.controller;

import jakarta.validation.Valid;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.htc.fleetmanagement.dto.DriverRequest;
import com.htc.fleetmanagement.dto.DriverResponse;
import com.htc.fleetmanagement.service.DriverService;
import com.htc.fleetmanagement.ratelimit.RateLimiter;

@RestController
@RequestMapping("/api/drivers")
@Validated
public class DriverController {
	
	@Autowired
	private DriverService driverService;
	
	
	@PostMapping("/register")
	public ResponseEntity<DriverResponse> registerDriver(@Valid @RequestBody DriverRequest request) {
		DriverResponse response = driverService.registerDriver(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DriverResponse> getDriverById(@PathVariable Integer id) {
		return driverService.findById(id)
				.map(response -> new ResponseEntity<>(response, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@RateLimiter(limit = 10, timeWindow = 60)
	@GetMapping
	public ResponseEntity<List<DriverResponse>> getAllDrivers() {
		List<DriverResponse> responses = driverService.findAll();
		return new ResponseEntity<>(responses, HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<DriverResponse> updateDriver(@PathVariable Integer id, @Valid @RequestBody DriverRequest request) {
		return driverService.findById(id)
				.map(existing -> {
					DriverResponse response = driverService.updateDriver(id, request);
					return new ResponseEntity<>(response, HttpStatus.OK);
				})
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<DriverResponse> partialUpdateDriver(@PathVariable Integer id, @Valid @RequestBody DriverRequest request) {
		return driverService.findById(id)
				.map(existing -> {
					DriverResponse response = driverService.partialUpdateDriver(id, request);
					return new ResponseEntity<>(response, HttpStatus.OK);
				})
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteDriver(@PathVariable Integer id) {
		return driverService.findById(id)
				.map(existing -> {
					driverService.deleteDriver(id);
					return new ResponseEntity<Void>(HttpStatus.OK);
				})
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
}
