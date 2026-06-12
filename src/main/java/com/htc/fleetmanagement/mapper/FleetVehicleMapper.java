package com.htc.fleetmanagement.mapper;

import com.htc.fleetmanagement.dto.FleetVehicleRequest;
import com.htc.fleetmanagement.dto.FleetVehicleResponse;
import com.htc.fleetmanagement.entity.FleetVehicle;
import com.htc.fleetmanagement.entity.CorporateClient;
import com.htc.fleetmanagement.entity.FleetManager;
import com.htc.fleetmanagement.util.VehicleStatus;
import com.htc.fleetmanagement.util.AuthenticationUtil;
import com.htc.fleetmanagement.repository.CorporateClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FleetVehicleMapper {
    
    @Autowired
    private CorporateClientRepository corporateClientRepository;
    
    @Autowired
    private AuthenticationUtil authenticationUtil;
    

    public FleetVehicleResponse toDto(FleetVehicle vehicle) {
        try {
            if (vehicle == null) {
                return null;
            }
            
            FleetVehicleResponse response = new FleetVehicleResponse();
            response.setVehicleId(vehicle.getVehicleId());
            response.setVin(vehicle.getVin());
            response.setMakeModel(vehicle.getMakeModel());
            response.setStatus(vehicle.getStatus() != null ? vehicle.getStatus().toString() : null);
            response.setCreatedAt(vehicle.getCreatedAt());
            response.setUpdatedAt(vehicle.getUpdatedAt());
            
            // Fetch complete client information from database if needed
            if (vehicle.getClient() != null) {
                CorporateClient client = vehicle.getClient();
                response.setClientId(client.getUserId());
                
                // If client name is null, fetch from database using client ID
                if (client.getCompanyName() == null && client.getUserId() != null) {
                    CorporateClient fullClient = corporateClientRepository.findById(client.getUserId()).orElse(null);
                    if (fullClient != null) {
                        response.setClientName(fullClient.getCompanyName());
                    }
                } else {
                    response.setClientName(client.getCompanyName());
                }
            }
            
            return response;
        } catch (Exception e) {
            System.err.println("Error converting vehicle to DTO: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to convert vehicle entity to response", e);
        }
    }
    

    public FleetVehicle toEntity(FleetVehicleRequest request) {
        try {
            if (request == null) {
                throw new IllegalArgumentException("Vehicle request cannot be null");
            }

            
            FleetVehicle vehicle = new FleetVehicle();
            vehicle.setVin(request.getVin());
            vehicle.setMakeModel(request.getMakeModel());
            vehicle.setStatus(VehicleStatus.ACTIVE);
            
            // Get the currently authenticated fleet manager and extract their client
            FleetManager currentFleetManager = authenticationUtil.getCurrentFleetManager();
            
            if (currentFleetManager == null) {
                throw new RuntimeException("Fleet Manager information not found in authentication context");
            }
            
            if (currentFleetManager.getClient() == null) {
                throw new RuntimeException("No client assigned to the current fleet manager");
            }
            
            Integer clientId = currentFleetManager.getClient().getUserId();
            
            // Fetch and attach complete client information
            if (clientId != null && clientId > 0) {
                CorporateClient client = corporateClientRepository.findById(clientId)
                    .orElseThrow(() -> new RuntimeException("Corporate Client with ID " + clientId + " not found"));
                vehicle.setClient(client);
                System.out.println("Vehicle assigned to client: " + client.getCompanyName() + " (ID: " + clientId + ")");
            } else {
                throw new RuntimeException("Invalid client ID retrieved from fleet manager");
            }
            
            return vehicle;
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid request for vehicle creation: " + e.getMessage());
            throw e;
        } catch (RuntimeException e) {
            System.err.println("Error converting request to vehicle entity: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected error in toEntity: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to convert request to vehicle entity", e);
        }
    }
    
}
