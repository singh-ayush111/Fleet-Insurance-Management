package com.htc.fleetmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.htc.fleetmanagement.dto.FleetClaimRequest;
import com.htc.fleetmanagement.dto.FleetClaimResponse;
import com.htc.fleetmanagement.entity.FleetClaim;
import com.htc.fleetmanagement.entity.FleetVehicle;
import com.htc.fleetmanagement.entity.Driver;
import com.htc.fleetmanagement.entity.CorporateClient;
import com.htc.fleetmanagement.exception.UnauthorizedAccessException;
import com.htc.fleetmanagement.mapper.FleetClaimMapper;
import com.htc.fleetmanagement.repository.FleetClaimRepository;
import com.htc.fleetmanagement.repository.FleetVehicleRepository;
import com.htc.fleetmanagement.repository.CorporateClientRepository;
import com.htc.fleetmanagement.service.FleetClaimService;
import com.htc.fleetmanagement.util.AuthenticationUtil;
import com.htc.fleetmanagement.util.ClaimStatus;
import com.htc.fleetmanagement.util.VehicleStatus;
import com.htc.fleetmanagement.exception.ResourceNotFoundException;
import com.itextpdf.text.DocumentException;


@Service
public class FleetClaimServiceImpl implements FleetClaimService {

	@Autowired
	private FleetClaimRepository fleetClaimRepo;

	@Autowired
	private FleetClaimMapper fleetClaimMapper;

	@Autowired
	private AuthenticationUtil authenticationUtil;

	@Autowired
	private FleetVehicleRepository vehicleRepo;

	@Autowired
	private RiskScore riskScore;

	@Autowired
	private EmailService emailService;

	@Autowired
	private CorporateClientRepository corporateClientRepository;

	@Override
	@Transactional
	public FleetClaimResponse registerFleetClaim(FleetClaimRequest request) {
		FleetClaim claim = fleetClaimMapper.toEntity(request);
		FleetClaim savedClaim = fleetClaimRepo.save(claim);
		return fleetClaimMapper.toDto(savedClaim);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<FleetClaimResponse> findById(Integer id) {
		Optional<FleetClaim> claim = fleetClaimRepo.findById(id);
		if (claim.isPresent()) {
			verifyDriverOwnership(claim.get());
		}
		return claim.map(fleetClaimMapper::toDto);
	}

	@Override
	@Transactional(readOnly = true)
	public List<FleetClaimResponse> findAll() {
		Driver currentDriver = authenticationUtil.getCurrentDriver();
		List<FleetClaim> claims = fleetClaimRepo.findAll();
		return claims.stream()
				.filter(claim -> claim.getDriver().getEmployeeId().equals(currentDriver.getEmployeeId()))
				.map(fleetClaimMapper::toDto)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public FleetClaimResponse updateFleetClaim(Integer id, FleetClaimRequest request) {
		Optional<FleetClaim> existingClaim = fleetClaimRepo.findById(id);
		if (existingClaim.isPresent()) {
			FleetClaim claim = existingClaim.get();
			verifyDriverOwnership(claim);
			if (request.getIncidentDate() != null) {
				claim.setIncidentDate(request.getIncidentDate());
			}
			if (request.getRepairCost() != null) {
				claim.setRepairCost(request.getRepairCost());
			}
			FleetClaim updatedClaim = fleetClaimRepo.save(claim);
			return fleetClaimMapper.toDto(updatedClaim);
		}
		return null;
	}

	@Override
	@Transactional
	public FleetClaimResponse partialUpdateFleetClaim(Integer id, FleetClaimRequest request) {
		Optional<FleetClaim> existingClaim = fleetClaimRepo.findById(id);
		if (existingClaim.isPresent()) {
			FleetClaim currentClaim = existingClaim.get();
			verifyDriverOwnership(currentClaim);

			if (request.getIncidentDate() != null) {
				currentClaim.setIncidentDate(request.getIncidentDate());
			}
			if (request.getRepairCost() != null) {
				currentClaim.setRepairCost(request.getRepairCost());
			}

			FleetClaim updatedClaim = fleetClaimRepo.save(currentClaim);
			return fleetClaimMapper.toDto(updatedClaim);
		}
		return null;
	}

	@Override
	@Transactional
	public void deleteFleetClaim(Integer id) {
		Optional<FleetClaim> claim = fleetClaimRepo.findById(id);
		if (claim.isPresent()) {
			verifyDriverOwnership(claim.get());
			fleetClaimRepo.deleteById(id);
		}
	}


	// checks if the currently authenticated driver is the owner of the claim.
	private void verifyDriverOwnership(FleetClaim claim) {
		try {
			Driver currentDriver = authenticationUtil.getCurrentDriver();
			if (!claim.getDriver().getEmployeeId().equals(currentDriver.getEmployeeId())) {
				throw new UnauthorizedAccessException(
						"You are not authorized to access this claim. Only the driver who created this claim can access it."
						);
			}
		} catch (UnauthorizedAccessException e) {
			throw e;
		}
	}



	// This method allows drivers to file a new claim for a specific vehicle. 
	// It checks that the vehicle belongs to the same company as the driver, and 
	// that the vehicle is not already inactive due to a previous claim. If the claim 
	// is successfully filed, it updates the vehicle status to INACTIVE and sends an 
	// email notification to the fleet manager.
	@Override
	@Transactional
	public FleetClaimResponse fileClaimAsDriver(FleetClaimRequest request) {
		try {
			Driver currentDriver = authenticationUtil.getCurrentDriver();

			FleetClaim claim = fleetClaimMapper.toEntity(request);
			claim.setDriver(currentDriver);
			claim.setStatus(ClaimStatus.PENDING);

			FleetVehicle vehicle = vehicleRepo.findById(request.getVehicleId())
					.orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

			if (!vehicle.getClient().getUserId().equals(currentDriver.getClient().getUserId())) {
				throw new UnauthorizedAccessException(
						"You can only file claims for vehicles belonging to your company."
						);
			}

			// Check vehicle status using switch statement
			switch (vehicle.getStatus()) {
			case ACTIVE:
				// Vehicle is active, proceed with claim filing
				break;
			case INACTIVE:
				throw new UnauthorizedAccessException(
						"Cannot file a claim for an inactive vehicle. This vehicle has already been used in a claim."
						);
			default:
				throw new UnauthorizedAccessException(
						"Vehicle has an invalid status for claim filing."
						);
			}

			claim.setVehicle(vehicle);

			// save first
			FleetClaim savedClaim = fleetClaimRepo.save(claim); 

			// Update vehicle status from ACTIVE to INACTIVE
			vehicle.setStatus(VehicleStatus.INACTIVE);
			vehicleRepo.save(vehicle);

			// update risk score
			riskScore.updateDriverRiskScore(currentDriver);                

			// send email notification to fleet manager
			emailService.sendClaimNotificationToManager(savedClaim, currentDriver);

			return fleetClaimMapper.toDto(savedClaim);
		} catch (UnauthorizedAccessException | ResourceNotFoundException e) {
			throw e;
		} catch (DocumentException e) {
			// Handle PDF generation errors
			System.out.println("Error in fileClaimAsDriver - PDF Generation: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Failed to generate claim notification", e);
		} catch (Exception e) {
			// Handle all other exceptions
			System.out.println("Error in fileClaimAsDriver: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Failed to file claim", e);
		}
	}


	// This method allows insurance agents to view all PENDING claims for a specific client.
	@Override
	@Transactional(readOnly = true)
	@PreAuthorize("hasRole('INSURANCE_AGENT')")
	public List<FleetClaimResponse> getClaimsByClientId(Integer clientId) {
		CorporateClient client = corporateClientRepository.findById(clientId)
				.orElseThrow(() -> new ResourceNotFoundException("Client not found"));

		List<FleetClaim> claims = fleetClaimRepo.findByClient(client);
		return claims.stream()
				.map(fleetClaimMapper::toDto)
				.collect(Collectors.toList());
	}


	// This method allows insurance agents to view only the PENDING claims for a 
	// specific client, which they can then approve or reject
	@Override
	@Transactional(readOnly = true)
	@PreAuthorize("hasRole('INSURANCE_AGENT')")
	public List<FleetClaimResponse> getPendingClaimsByClientId(Integer clientId) {
		CorporateClient client = corporateClientRepository.findById(clientId)
				.orElseThrow(() -> new ResourceNotFoundException("Client not found"));

		List<FleetClaim> claims = fleetClaimRepo.findByClientAndStatus(client, ClaimStatus.PENDING);
		return claims.stream()
				.map(fleetClaimMapper::toDto)
				.collect(Collectors.toList());
	}


	// This method allows insurance agents to update the status of a claim from 
	// PENDING to either APPROVED or REJECTED
	@Override
	@Transactional
	@PreAuthorize("hasRole('INSURANCE_AGENT')")
	public FleetClaimResponse updateClaimStatus(Integer claimId, ClaimStatus newStatus) {
		try {
			FleetClaim claim = fleetClaimRepo.findById(claimId)
					.orElseThrow(() -> new ResourceNotFoundException("Claim not found"));

			// Only PENDING claims can be changed
			if (claim.getStatus() != ClaimStatus.PENDING) {
				throw new UnauthorizedAccessException(
						"Only PENDING claims can be updated. Current status: " + claim.getStatus()
						);
			}

			// Validate new status using switch statement
			switch (newStatus) {
			case APPROVED:
			case REJECTED:
				// Valid statuses - proceed with update
				break;
			default:
				throw new UnauthorizedAccessException(
						"Invalid status. Only APPROVED or REJECTED are allowed."
						);
			}

			// Update claim status
			claim.setStatus(newStatus);
			FleetClaim updatedClaim = fleetClaimRepo.save(claim);

			// Change vehicle status back to ACTIVE when claim is approved or rejected
			FleetVehicle vehicle = claim.getVehicle();
			switch (newStatus) {
			case APPROVED:
			case REJECTED:
				if (vehicle != null) {
					vehicle.setStatus(VehicleStatus.ACTIVE);
					vehicleRepo.save(vehicle);
				}
				break;
			default:
				break;
			}

			// Send email notification to fleet manager about the status update
			emailService.sendClaimStatusUpdateToManager(updatedClaim, newStatus);

			return fleetClaimMapper.toDto(updatedClaim);
		} catch (Exception e) {
			if (e instanceof UnauthorizedAccessException || e instanceof ResourceNotFoundException) {
				throw e;
			}
			System.out.println("Error in updateClaimStatus: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Failed to update claim status", e);
		}
	}


}