package com.htc.fleetmanagement.service;

import java.util.List;
import java.util.Optional;

import com.htc.fleetmanagement.dto.FleetClaimRequest;
import com.htc.fleetmanagement.dto.FleetClaimResponse;
import com.htc.fleetmanagement.util.ClaimStatus;

public interface FleetClaimService {
	FleetClaimResponse registerFleetClaim(FleetClaimRequest request);
	
	Optional<FleetClaimResponse> findById(Integer id);
	
	List<FleetClaimResponse> findAll();
	
	FleetClaimResponse updateFleetClaim(Integer id, FleetClaimRequest request);
	
	FleetClaimResponse partialUpdateFleetClaim(Integer id, FleetClaimRequest request);
	
	void deleteFleetClaim(Integer id);
	

	
	
	FleetClaimResponse fileClaimAsDriver(FleetClaimRequest request);
	
	List<FleetClaimResponse> getClaimsByClientId(Integer clientId);

	List<FleetClaimResponse> getPendingClaimsByClientId(Integer clientId);

	FleetClaimResponse updateClaimStatus(Integer claimId, ClaimStatus newStatus);
}

