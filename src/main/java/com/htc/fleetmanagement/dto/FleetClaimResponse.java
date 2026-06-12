package com.htc.fleetmanagement.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PastOrPresent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FleetClaimResponse {

    private Integer claimId;
    private Integer vehicleId;
    private String vehicleVin;
    private String vehicleMakeModel;
    private Integer driverId;
    private String driverName;
    private LocalDate incidentDate;
    private BigDecimal repairCost;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
