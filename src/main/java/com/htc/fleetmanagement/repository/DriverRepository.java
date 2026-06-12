package com.htc.fleetmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.htc.fleetmanagement.entity.Driver;

public interface DriverRepository extends JpaRepository<Driver, Integer> {


	    List<Driver> findByRiskScoreGreaterThan(Float score);

	    List<Driver> findByRiskScoreLessThanEqual(Float score);

	    Optional<Driver> findByLicenseNumber(String licenseNumber);
	    
	    Optional<Driver> findByUsername(String username);
	    
	    Optional<Driver> findByEmployeeId(Integer employeeId);

}
