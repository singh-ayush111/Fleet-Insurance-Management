package com.htc.fleetmanagement.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.htc.fleetmanagement.dto.UserPrincipal;
import com.htc.fleetmanagement.entity.Admin;
import com.htc.fleetmanagement.entity.CorporateClient;
import com.htc.fleetmanagement.entity.Driver;
import com.htc.fleetmanagement.entity.Employee;
import com.htc.fleetmanagement.entity.FleetManager;
import com.htc.fleetmanagement.entity.InsuranceAgent;
import com.htc.fleetmanagement.entity.User;
import com.htc.fleetmanagement.repository.AdminRepository;
import com.htc.fleetmanagement.repository.CorporateClientRepository;
import com.htc.fleetmanagement.repository.DriverRepository;
import com.htc.fleetmanagement.repository.EmployeeRepository;
import com.htc.fleetmanagement.repository.FleetManagerRepository;
import com.htc.fleetmanagement.repository.InsuranceAgentRepository;
import com.htc.fleetmanagement.repository.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private DriverRepository driverRepository;
	
	@Autowired
	private FleetManagerRepository fleetManagerRepository;
	
	@Autowired
	private CorporateClientRepository corporateClientRepository;
	
	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private InsuranceAgentRepository insuranceAgentRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("Loading user by username: " + username);
		
		// Check Driver repository first (Driver extends Employee)
		Driver driver = driverRepository.findByUsername(username).orElse(null);
		if (driver != null) {
			System.out.println("User found in Driver repository");
			return new UserPrincipal(driver);
		}
		
		// Check FleetManager repository (FleetManager extends Employee)
		FleetManager fleetManager = fleetManagerRepository.findByUsername(username).orElse(null);
		if (fleetManager != null) {
			System.out.println("User found in FleetManager repository");
			return new UserPrincipal(fleetManager);
		}
		
		// Check CorporateClient repository (CorporateClient extends User)
		CorporateClient corporateClient = corporateClientRepository.findByUsername(username).orElse(null);
		if (corporateClient != null) {
			System.out.println("User found in CorporateClient repository");
			return new UserPrincipal(corporateClient);
		}
		
		// Check Admin repository (Admin extends User)
		Admin admin = adminRepository.findByUsername(username).orElse(null);
		if (admin != null) {
			System.out.println("User found in Admin repository");
			return new UserPrincipal(admin);
		}
		
		// Check InsuranceAgent repository (InsuranceAgent extends User)
		InsuranceAgent insuranceAgent = insuranceAgentRepository.findByUsername(username).orElse(null);
		if (insuranceAgent != null) {
			System.out.println("User found in InsuranceAgent repository");
			return new UserPrincipal(insuranceAgent);
		}
		
		// Check User repository (generic User)
		User user = userRepository.findByUsername(username).orElse(null);
		if (user != null) {
			System.out.println("User found in User repository");
			return new UserPrincipal(user);
		}
		
		// Check Employee repository (generic Employee)
		Employee employee = employeeRepository.findByUsername(username).orElse(null);
		if (employee != null) {
			System.out.println("User found in Employee repository");
			return new UserPrincipal(employee);
		}
		
		// If not found anywhere, throw exception
		System.out.println("User not found with username: " + username);
		throw new UsernameNotFoundException("User not found with username: " + username);
	}

}
