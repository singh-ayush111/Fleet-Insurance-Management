package com.htc.fleetmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.htc.fleetmanagement.entity.Driver;
import com.htc.fleetmanagement.entity.FleetClaim;
import com.htc.fleetmanagement.entity.CorporateClient;
import com.htc.fleetmanagement.util.ClaimStatus;

public interface FleetClaimRepository extends JpaRepository<FleetClaim, Integer>{
	
    int countByDriver(Driver driver);

	List<FleetClaim> findByDriver(Driver driver);
	
	@Query("SELECT c FROM FleetClaim c WHERE c.vehicle.client = :client")
	List<FleetClaim> findByClient(@Param("client") CorporateClient client);
	
	@Query("SELECT c FROM FleetClaim c WHERE c.vehicle.client = :client AND c.status = :status")
	List<FleetClaim> findByClientAndStatus(@Param("client") CorporateClient client, @Param("status") ClaimStatus status);

}
