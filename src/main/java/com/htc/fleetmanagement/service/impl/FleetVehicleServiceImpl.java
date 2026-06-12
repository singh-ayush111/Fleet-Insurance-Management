package com.htc.fleetmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.htc.fleetmanagement.dto.FleetVehicleRequest;
import com.htc.fleetmanagement.dto.FleetVehicleResponse;
import com.htc.fleetmanagement.entity.FleetVehicle;
import com.htc.fleetmanagement.mapper.FleetVehicleMapper;
import com.htc.fleetmanagement.repository.FleetVehicleRepository;
import com.htc.fleetmanagement.service.FleetVehicleService;

@Service
public class FleetVehicleServiceImpl implements FleetVehicleService {
	
	@Autowired
	private FleetVehicleRepository fleetVehicleRepo;
	
	@Autowired
	private FleetVehicleMapper fleetVehicleMapper;

	@Override
	@Transactional
	public FleetVehicleResponse addFleetVehicle(FleetVehicleRequest request) {
		FleetVehicle vehicle = fleetVehicleMapper.toEntity(request);
		FleetVehicle savedVehicle = fleetVehicleRepo.save(vehicle);
		return fleetVehicleMapper.toDto(savedVehicle);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<FleetVehicleResponse> findById(Integer id) {
		return fleetVehicleRepo.findById(id).map(fleetVehicleMapper::toDto);
	}

	@Override
	@Transactional(readOnly = true)
	public List<FleetVehicleResponse> findAll() {
		List<FleetVehicle> vehicles = fleetVehicleRepo.findAll();
		return vehicles.stream()
				.map(fleetVehicleMapper::toDto)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public FleetVehicleResponse updateFleetVehicle(Integer id, FleetVehicleRequest request) {
		Optional<FleetVehicle> existingVehicle = fleetVehicleRepo.findById(id);
		if (existingVehicle.isPresent()) {
			FleetVehicle vehicle = existingVehicle.get();
			
			if (request.getVin() != null) {
				vehicle.setVin(request.getVin());
			}
			if (request.getMakeModel() != null) {
				vehicle.setMakeModel(request.getMakeModel());
			}
//			if (request.getClientId() != null) {
//				CorporateClient client = new CorporateClient();
//				client.setUserId(request.getClientId());
//				vehicle.setClient(client);
//			}
			FleetVehicle updatedVehicle = fleetVehicleRepo.save(vehicle);
			return fleetVehicleMapper.toDto(updatedVehicle);
		}
		return null;
	}

	@Override
	@Transactional
	public FleetVehicleResponse partialUpdateFleetVehicle(Integer id, FleetVehicleRequest request) {
		Optional<FleetVehicle> existingVehicle = fleetVehicleRepo.findById(id);
		if (existingVehicle.isPresent()) {
			FleetVehicle currentVehicle = existingVehicle.get();
			
			if (request.getVin() != null) {
				currentVehicle.setVin(request.getVin());
			}
			if (request.getMakeModel() != null) {
				currentVehicle.setMakeModel(request.getMakeModel());
			}
			
			FleetVehicle updatedVehicle = fleetVehicleRepo.save(currentVehicle);
			return fleetVehicleMapper.toDto(updatedVehicle);
		}
		return null;
	}

	@Override
	@Transactional
	public void deleteFleetVehicle(Integer id) {
		fleetVehicleRepo.deleteById(id);
	}
}
