package com.htc.fleetmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.htc.fleetmanagement.entity.FleetVehicle;

public interface FleetVehicleRepository extends JpaRepository<FleetVehicle, Integer>{

}
