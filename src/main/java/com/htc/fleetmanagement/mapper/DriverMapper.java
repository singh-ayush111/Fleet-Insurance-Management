package com.htc.fleetmanagement.mapper;

import com.htc.fleetmanagement.dto.DriverRequest;
import com.htc.fleetmanagement.dto.DriverResponse;
import com.htc.fleetmanagement.entity.Driver;
import com.htc.fleetmanagement.entity.CorporateClient;
import com.htc.fleetmanagement.exception.CorporateClientNotFound;
import com.htc.fleetmanagement.repository.CorporateClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DriverMapper {
    
    @Autowired
    private CorporateClientRepository corporateClientRepository;
    
    public DriverResponse toDto(Driver driver) {
         if (driver == null) {
             return null;
         }
         
         DriverResponse response = new DriverResponse();
         response.setEmployeeId(driver.getEmployeeId());
         response.setName(driver.getName());
         response.setLicenseNumber(driver.getLicenseNumber());
         response.setRiskScore(driver.getRiskScore());
         response.setRole(driver.getRole());
         response.setCreatedAt(driver.getCreatedAt());
         response.setUpdatedAt(driver.getUpdatedAt());
         response.setEmail(driver.getEmail());
         response.setUsername(driver.getUsername());
        
         // Set manager ID if fleet manager is assigned
         if (driver.getFleetManager() != null) {
             response.setManagerId(driver.getFleetManager().getEmployeeId());
         }
         
         if (driver.getClient() != null) {
             response.setClientId(driver.getClient().getUserId());
             response.setClientName(driver.getClient().getCompanyName());
         }
         
         return response;
     }
    
    public Driver toEntity(DriverRequest request) {
        if (request == null) {
            return null;
        }
        
        Driver driver = new Driver();
        driver.setName(request.getName());
        driver.setLicenseNumber(request.getLicenseNumber());
        driver.setRiskScore(request.getRiskScore());
        driver.setRole(request.getRole());
        driver.setEmail(request.getEmail());
        driver.setPassword(request.getPassword());
        driver.setUsername(request.getUsername());
        
        if (request.getClientId() != null) {
            CorporateClient client = corporateClientRepository.findById(request.getClientId())
                .orElseThrow(() -> new CorporateClientNotFound(
                    "Corporate Client with ID " + request.getClientId() + " does not exist. " +
                    "Driver cannot be created without a valid client."
                ));
            
            driver.setClient(client);
        } else {
            throw new CorporateClientNotFound(
                "Client ID is required to create a driver. Please provide a valid corporate client ID."
            );
        }
        
        return driver;
    }
}
