package com.htc.fleetmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.htc.fleetmanagement.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	
	Optional<Employee> findByUsername(String username);
}
