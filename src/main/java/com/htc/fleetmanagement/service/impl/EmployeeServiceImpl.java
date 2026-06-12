package com.htc.fleetmanagement.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.htc.fleetmanagement.entity.Employee;
import com.htc.fleetmanagement.repository.EmployeeRepository;
import com.htc.fleetmanagement.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository emprepo;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	@Transactional
	public Employee registerNewEmployee(Employee employee) {
		employee.setPassword(bCryptPasswordEncoder.encode(employee.getPassword()));
		return emprepo.save(employee);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Employee> findById(Integer id) {
		return emprepo.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Employee> findAll() {
		return emprepo.findAll();
	}

	@Override
	@Transactional
	public Employee updateEmployee(Employee employee) {
		return emprepo.save(employee);
	}

	@Override
	@Transactional
	public void deleteEmployee(Integer id) {
		emprepo.deleteById(id);
	}
}

