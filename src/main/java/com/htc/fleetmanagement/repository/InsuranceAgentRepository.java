package com.htc.fleetmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.htc.fleetmanagement.entity.InsuranceAgent;
import com.htc.fleetmanagement.util.AccountStatus;


@Repository
public interface InsuranceAgentRepository  extends JpaRepository<InsuranceAgent, Integer>{
	
	List<InsuranceAgent> findByStatus(AccountStatus status);
	
	List<InsuranceAgent> findByUserId(Integer id);
	
	Optional<InsuranceAgent> findByUsername(String username);

}
