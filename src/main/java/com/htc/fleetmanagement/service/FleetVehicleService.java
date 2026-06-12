package com.htc.fleetmanagement.service;

import java.util.List;
import java.util.Optional;

import com.htc.fleetmanagement.dto.FleetVehicleRequest;
import com.htc.fleetmanagement.dto.FleetVehicleResponse;

public interface FleetVehicleService {
	FleetVehicleResponse addFleetVehicle(FleetVehicleRequest request);
	
	Optional<FleetVehicleResponse> findById(Integer id);
	
	List<FleetVehicleResponse> findAll();
	
	FleetVehicleResponse updateFleetVehicle(Integer id, FleetVehicleRequest request);
	
	FleetVehicleResponse partialUpdateFleetVehicle(Integer id, FleetVehicleRequest request);
	
	void deleteFleetVehicle(Integer id);
}
