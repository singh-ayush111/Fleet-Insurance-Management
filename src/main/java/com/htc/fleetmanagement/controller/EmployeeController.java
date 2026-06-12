package com.htc.fleetmanagement.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.htc.fleetmanagement.entity.Employee;
import com.htc.fleetmanagement.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@PostMapping("/register")
	public ResponseEntity<Employee> registerEmployee(@RequestBody Employee employee) {
		Employee savedEmployee = employeeService.registerNewEmployee(employee);
		
		return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
	}
	
	
	
	
	@GetMapping("/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable Integer id) {
		Optional<Employee> employee = employeeService.findById(id);
		
		return employee.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	
	
	
	@GetMapping
	public ResponseEntity<List<Employee>> getAllEmployees() {
		List<Employee> employees = employeeService.findAll();
		return new ResponseEntity<>(employees, HttpStatus.OK);
	}
	
	
	
	@PutMapping("/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable Integer id, @RequestBody Employee employee) {
		Optional<Employee> existingEmployee = employeeService.findById(id);
		if (existingEmployee.isPresent()) {
			Employee updatedEmployee = employeeService.updateEmployee(employee);
			return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	
	
	@PatchMapping("/{id}")
	public ResponseEntity<Employee> partialUpdateEmployee(@PathVariable Integer id, @RequestBody Employee employee) {
		Optional<Employee> existingEmployee = employeeService.findById(id);
		if (existingEmployee.isPresent()) {
			Employee currentEmployee = existingEmployee.get();
			
			if (employee.getName() != null) {
				currentEmployee.setName(employee.getName());
			}
			if (employee.getLicenseNumber() != null) {
				currentEmployee.setLicenseNumber(employee.getLicenseNumber());
			}
			if (employee.getClient() != null) {
				currentEmployee.setClient(employee.getClient());
			}
			
			Employee updatedEmployee = employeeService.updateEmployee(currentEmployee);
			return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEmployee(@PathVariable Integer id) {
		Optional<Employee> employee = employeeService.findById(id);
		if (employee.isPresent()) {
			employeeService.deleteEmployee(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
