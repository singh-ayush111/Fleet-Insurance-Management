package com.htc.fleetmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.htc.fleetmanagement.util.ClaimStatus;

@Entity
@Table(name = "351_fleet_claim_351")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FleetClaim extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "claim_id")
    private Integer claimId;

    @JsonIgnoreProperties({"claims", "hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private FleetVehicle vehicle;

    @JsonIgnoreProperties({"claims", "hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @Column(name = "incident_date", nullable = false)
    private LocalDate incidentDate;

    @Column(name = "repair_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal repairCost;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ClaimStatus status;    
}