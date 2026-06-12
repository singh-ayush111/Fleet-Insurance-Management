package com.htc.fleetmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "351_driver_351")
@PrimaryKeyJoinColumn(name = "driver_id")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Driver extends Employee {

	@Column(name = "risk_score", nullable = false)
    private Float riskScore;

	@JsonIgnore
    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
    private List<FleetClaim> claims;
	
	@JsonIgnoreProperties({"drivers", "hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id") 
    private FleetManager fleetManager;

}