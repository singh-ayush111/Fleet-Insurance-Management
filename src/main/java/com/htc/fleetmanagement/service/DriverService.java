package com.htc.fleetmanagement.service;

import java.util.List;
import java.util.Optional;

import com.htc.fleetmanagement.dto.DriverRequest;
import com.htc.fleetmanagement.dto.DriverResponse;

public interface DriverService {
	DriverResponse registerDriver(DriverRequest request);
	
	Optional<DriverResponse> findById(Integer id);
	
	List<DriverResponse> findAll();
	
	DriverResponse updateDriver(Integer id, DriverRequest request);
	
	DriverResponse partialUpdateDriver(Integer id, DriverRequest request);
	
	void deleteDriver(Integer id);
	
	List<DriverResponse> findByRiskScoreGreaterThan(Float score);
	
	List<DriverResponse> findByRiskScoreLessThanEqual(Float score);
	
	Optional<DriverResponse> findByLicenseNumber(String licenseNumber);
}
