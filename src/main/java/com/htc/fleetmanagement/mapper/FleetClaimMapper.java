package com.htc.fleetmanagement.mapper;

import com.htc.fleetmanagement.dto.FleetClaimRequest;
import com.htc.fleetmanagement.dto.FleetClaimResponse;
import com.htc.fleetmanagement.entity.FleetClaim;
import com.htc.fleetmanagement.util.ClaimStatus;
import org.springframework.stereotype.Component;

@Component
public class FleetClaimMapper {

    public FleetClaim toEntity(FleetClaimRequest request) {
        if (request == null) return null;

        FleetClaim claim = new FleetClaim();
        claim.setIncidentDate(request.getIncidentDate());
        claim.setRepairCost(request.getRepairCost());
        return claim;
    }

    public FleetClaimResponse toDto(FleetClaim claim) {
        if (claim == null) return null;

        FleetClaimResponse response = new FleetClaimResponse();
        response.setClaimId(claim.getClaimId());
        response.setIncidentDate(claim.getIncidentDate());
        response.setRepairCost(claim.getRepairCost());
        response.setStatus(claim.getStatus() != null ? claim.getStatus().toString() : null);
        response.setCreatedAt(claim.getCreatedAt());
        response.setUpdatedAt(claim.getUpdatedAt());

        if (claim.getVehicle() != null) {
            response.setVehicleId(claim.getVehicle().getVehicleId());
            response.setVehicleVin(claim.getVehicle().getVin());
            response.setVehicleMakeModel(claim.getVehicle().getMakeModel());
        }

        if (claim.getDriver() != null) {
            response.setDriverId(claim.getDriver().getEmployeeId());
            response.setDriverName(claim.getDriver().getName());
        }

        return response;
    }

}
