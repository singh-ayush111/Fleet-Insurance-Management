package com.htc.fleetmanagement.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.htc.fleetmanagement.dto.DriverRequest;
import com.htc.fleetmanagement.dto.DriverResponse;
import com.htc.fleetmanagement.dto.UserPrincipal;
import com.htc.fleetmanagement.entity.Driver;
import com.htc.fleetmanagement.entity.FleetManager;
import com.htc.fleetmanagement.mapper.DriverMapper;
import com.htc.fleetmanagement.repository.DriverRepository;
import com.htc.fleetmanagement.repository.FleetManagerRepository;
import com.htc.fleetmanagement.service.DriverService;


@Service
public class DriverServiceImpl implements DriverService{

	@Autowired
	private DriverRepository driverrepo;
	
	@Autowired
	private DriverMapper driverMapper;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private FleetManagerRepository fleetManagerRepository;

	@Override
	@Transactional
	public DriverResponse registerDriver(DriverRequest request) {
		Driver driver = driverMapper.toEntity(request);
		driver.setPassword(passwordEncoder.encode(driver.getPassword()));
		
		// Get the authenticated Fleet Manager from SecurityContext
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
			UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
			Integer fleetManagerId = principal.getId();
			
			// Fetch the FleetManager entity and set it as the manager
			FleetManager fleetManager = fleetManagerRepository.findById(fleetManagerId).orElse(null);
			if (fleetManager != null) {
				driver.setFleetManager(fleetManager);
			}
		}
		
		Driver savedDriver = driverrepo.save(driver);
		return driverMapper.toDto(savedDriver);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<DriverResponse> findById(Integer id) {
		return driverrepo.findById(id).map(driverMapper::toDto);
	}

	@Override
	@Transactional(readOnly = true)
	public List<DriverResponse> findAll() {
		List<Driver> drivers = driverrepo.findAll();
		return drivers.stream()
				.map(driverMapper::toDto)
				.toList();
	}

	@Override
	@Transactional
	public DriverResponse updateDriver(Integer id, DriverRequest request) {
		Driver driver = driverMapper.toEntity(request);
		driver.setEmployeeId(id);
		Driver updatedDriver = driverrepo.save(driver);
		return driverMapper.toDto(updatedDriver);
	}

	@Override
	@Transactional
	public DriverResponse partialUpdateDriver(Integer id, DriverRequest request) {
		Optional<Driver> existingDriver = driverrepo.findById(id);
		if (existingDriver.isPresent()) {
			Driver currentDriver = existingDriver.get();
			
			if (request.getName() != null) {
				currentDriver.setName(request.getName());
			}
			if (request.getLicenseNumber() != null) {
				currentDriver.setLicenseNumber(request.getLicenseNumber());
			}
			
			Driver updatedDriver = driverrepo.save(currentDriver);
			return driverMapper.toDto(updatedDriver);
		}
		return null;
	}

	@Override
	@Transactional
	public void deleteDriver(Integer id) {
		driverrepo.deleteById(id);
	}

	//cache the results of this method based on the risk score parameter
	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "driversByRiskScoreGreaterThan", key = "#score")
	public List<DriverResponse> findByRiskScoreGreaterThan(Float score) {
		List<Driver> drivers = driverrepo.findByRiskScoreGreaterThan(score);
		return drivers.stream()
				.map(driverMapper::toDto)
				.toList();
	}

	//cache the results of this method based on the risk score parameter
	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "driversByRiskScoreLessThanEqual", key = "#score")
	public List<DriverResponse> findByRiskScoreLessThanEqual(Float score) {
		List<Driver> drivers = driverrepo.findByRiskScoreLessThanEqual(score);
		return drivers.stream()
				.map(driverMapper::toDto)
				.toList();
	}

	//cache the results of this method based on the license number parameter
	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "driversByLicenseNumber", key = "#licenseNumber")
	public Optional<DriverResponse> findByLicenseNumber(String licenseNumber) {
		return driverrepo.findByLicenseNumber(licenseNumber).map(driverMapper::toDto);
	}
}

