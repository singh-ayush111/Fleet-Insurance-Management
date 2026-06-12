package com.htc.fleetmanagement.service;

import java.util.List;
import java.util.Optional;

import com.htc.fleetmanagement.dto.FleetManagerRequest;
import com.htc.fleetmanagement.dto.FleetManagerResponse;

public interface FleetManagerService {
	FleetManagerResponse registerFleetManager(FleetManagerRequest request);
	
	Optional<FleetManagerResponse> findById(Integer id);
	
	List<FleetManagerResponse> findAll();
	
	FleetManagerResponse updateFleetManager(Integer id, FleetManagerRequest request);
	
	FleetManagerResponse partialUpdateFleetManager(Integer id, FleetManagerRequest request);
	
	void deleteFleetManager(Integer id);
	
	Optional<FleetManagerResponse> findByLicenseNumber(String licenseNumber);
	
	List<FleetManagerResponse> findByNameContainingIgnoreCase(String name);
}
