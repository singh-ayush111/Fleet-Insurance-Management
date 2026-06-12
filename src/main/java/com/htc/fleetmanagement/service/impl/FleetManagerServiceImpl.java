package com.htc.fleetmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.htc.fleetmanagement.dto.FleetManagerRequest;
import com.htc.fleetmanagement.dto.FleetManagerResponse;
import com.htc.fleetmanagement.entity.FleetManager;
import com.htc.fleetmanagement.mapper.FleetManagerMapper;
import com.htc.fleetmanagement.repository.FleetManagerRepository;
import com.htc.fleetmanagement.service.FleetManagerService;

@Service
public class FleetManagerServiceImpl implements FleetManagerService {
	
	@Autowired
	private FleetManagerRepository fleetManagerRepo;
	
	@Autowired
	private FleetManagerMapper fleetManagerMapper;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public FleetManagerResponse registerFleetManager(FleetManagerRequest request) {
		FleetManager manager = fleetManagerMapper.toEntity(request);
		manager.setPassword(passwordEncoder.encode(manager.getPassword()));
		FleetManager savedManager = fleetManagerRepo.save(manager);
		return fleetManagerMapper.toDto(savedManager);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<FleetManagerResponse> findById(Integer id) {
		return fleetManagerRepo.findById(id).map(fleetManagerMapper::toDto);
	}

	@Override
	@Transactional(readOnly = true)
	public List<FleetManagerResponse> findAll() {
		List<FleetManager> managers = fleetManagerRepo.findAll();
		return managers.stream()
				.map(fleetManagerMapper::toDto)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public FleetManagerResponse updateFleetManager(Integer id, FleetManagerRequest request) {
		FleetManager manager = fleetManagerMapper.toEntity(request);
		manager.setEmployeeId(id);
		FleetManager updatedManager = fleetManagerRepo.save(manager);
		return fleetManagerMapper.toDto(updatedManager);
	}

	@Override
	@Transactional
	public FleetManagerResponse partialUpdateFleetManager(Integer id, FleetManagerRequest request) {
		Optional<FleetManager> existingManager = fleetManagerRepo.findById(id);
		if (existingManager.isPresent()) {
			FleetManager currentManager = existingManager.get();
			
			if (request.getName() != null) {
				currentManager.setName(request.getName());
			}
			if (request.getLicenseNumber() != null) {
				currentManager.setLicenseNumber(request.getLicenseNumber());
			}
			
			FleetManager updatedManager = fleetManagerRepo.save(currentManager);
			return fleetManagerMapper.toDto(updatedManager);
		}
		return null;
	}

	@Override
	@Transactional
	public void deleteFleetManager(Integer id) {
		fleetManagerRepo.deleteById(id);
	}

	
	@Override
	@Transactional(readOnly = true)
	public Optional<FleetManagerResponse> findByLicenseNumber(String licenseNumber) {
		return fleetManagerRepo.findByLicenseNumber(licenseNumber).map(fleetManagerMapper::toDto);
	}

	@Override
	@Transactional(readOnly = true)
	public List<FleetManagerResponse> findByNameContainingIgnoreCase(String name) {
		List<FleetManager> managers = fleetManagerRepo.findByNameContainingIgnoreCase(name);
		return managers.stream()
				.map(fleetManagerMapper::toDto)
				.collect(Collectors.toList());
	}
}
