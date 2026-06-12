package com.htc.fleetmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FleetVehicleRequest {
    
//    @NotNull(message = "Client ID cannot be null")
//    @Positive(message = "Client ID must be positive")
//    private Integer clientId;
    
    @NotBlank(message = "VIN cannot be blank")
    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "VIN must be 17 alphanumeric characters (excluding I, O, Q)")
    private String vin;
    
    @NotBlank(message = "Make/Model cannot be blank")
    @Size(min = 2, max = 100, message = "Make/Model must be between 2 and 100 characters")
    private String makeModel;

}
