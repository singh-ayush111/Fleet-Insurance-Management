package com.htc.fleetmanagement.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PastOrPresent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FleetClaimRequest {

    @NotNull(message = "Vehicle ID cannot be null")
    @Positive(message = "Vehicle ID must be positive")
    private Integer vehicleId;

    @NotNull(message = "Incident date cannot be null")
    @PastOrPresent(message = "Incident date cannot be in the future")
    private LocalDate incidentDate;

    @NotNull(message = "Repair cost cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Repair cost must be greater than 0")
    private BigDecimal repairCost;
}
