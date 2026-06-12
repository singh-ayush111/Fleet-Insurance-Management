package com.htc.fleetmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.htc.fleetmanagement.entity.FleetManager;

public interface FleetManagerRepository extends JpaRepository<FleetManager, Integer>{
	

    Optional<FleetManager> findByLicenseNumber(String licenseNumber);

    List<FleetManager> findByNameContainingIgnoreCase(String name);
    
    Optional<FleetManager> findByUsername(String username);
    
    Optional<FleetManager> findByClient_UserId(Integer clientId);

}
