package com.htc.fleetmanagement.service;

import java.util.List;
import java.util.Optional;

import com.htc.fleetmanagement.entity.Employee;

public interface EmployeeService {
	Employee registerNewEmployee(Employee employee);
	
	Optional<Employee> findById(Integer id);
	
	List<Employee> findAll();
	
	Employee updateEmployee(Employee employee);
	
	void deleteEmployee(Integer id);
}
