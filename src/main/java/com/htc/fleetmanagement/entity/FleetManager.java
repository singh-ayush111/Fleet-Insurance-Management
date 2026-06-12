package com.htc.fleetmanagement.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "351_fleet_manager_351")
@PrimaryKeyJoinColumn(name = "manager_id")
@Getter
@Setter	
public class FleetManager extends Employee {

	@JsonIgnore
    @OneToMany(mappedBy = "fleetManager", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Driver> drivers;
	
}