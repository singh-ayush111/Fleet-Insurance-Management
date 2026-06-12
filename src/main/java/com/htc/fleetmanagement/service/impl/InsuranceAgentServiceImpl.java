package com.htc.fleetmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.htc.fleetmanagement.dto.InsuranceAgentRequest;
import com.htc.fleetmanagement.dto.InsuranceAgentResponse;
import com.htc.fleetmanagement.entity.InsuranceAgent;
import com.htc.fleetmanagement.exception.InsuranceAgentNotFoundException;
import com.htc.fleetmanagement.mapper.InsuranceAgentMapper;
import com.htc.fleetmanagement.repository.InsuranceAgentRepository;
import com.htc.fleetmanagement.service.InsuranceAgentService;
import com.htc.fleetmanagement.util.AccountStatus;

@Service
public class InsuranceAgentServiceImpl implements InsuranceAgentService {
	
	@Autowired
	private InsuranceAgentRepository insuranceAgentRepo;
	
	@Autowired
	private InsuranceAgentMapper insuranceAgentMapper;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	@Transactional
	public InsuranceAgentResponse registerInsuranceAgent(InsuranceAgentRequest request) {
		InsuranceAgent agent = insuranceAgentMapper.toEntity(request);
	    agent.setUserId(null);
	    agent.setPassword(bCryptPasswordEncoder.encode(agent.getPassword()));
		InsuranceAgent savedAgent = insuranceAgentRepo.save(agent);
		return insuranceAgentMapper.toDto(savedAgent);
	}

	@Override
	@Transactional(readOnly = true)
	public InsuranceAgentResponse findById(Integer id) throws InsuranceAgentNotFoundException {
		Optional<InsuranceAgent> agent = insuranceAgentRepo.findById(id);
		if (agent.isPresent()) {
			return insuranceAgentMapper.toDto(agent.get());
		}
		throw new InsuranceAgentNotFoundException("Insurance Agent with ID " + id + " not found");
	}

	@Override
	@Transactional(readOnly = true)
	public List<InsuranceAgentResponse> findAll() {
		List<InsuranceAgent> agents = insuranceAgentRepo.findAll();
		return agents.stream()
				.map(insuranceAgentMapper::toDto)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public InsuranceAgentResponse updateInsuranceAgent(Integer id, InsuranceAgentRequest request) throws InsuranceAgentNotFoundException {
		if (insuranceAgentRepo.existsById(id)) {
			InsuranceAgent agent = insuranceAgentMapper.toEntity(request);
			agent.setUserId(id);
			InsuranceAgent updatedAgent = insuranceAgentRepo.save(agent);
			return insuranceAgentMapper.toDto(updatedAgent);
		}
		throw new InsuranceAgentNotFoundException("Insurance Agent with ID " + id + " not found");
	}

	@Override
	@Transactional
	public InsuranceAgentResponse partialUpdateInsuranceAgent(Integer id, InsuranceAgentRequest request) throws InsuranceAgentNotFoundException {
		Optional<InsuranceAgent> agent = insuranceAgentRepo.findById(id);
		if (agent.isPresent()) {
			InsuranceAgent currentAgent = agent.get();
			
//			if (request.getUsername() != null) {
//				currentAgent.setUsername(request.getUsername());
//			}
			
			if (request.getPassword() != null) {
				currentAgent.setPassword(request.getPassword());
			}
			if (request.getStatus() != null) {
				currentAgent.setStatus(request.getStatus());
			}
			
			InsuranceAgent updatedAgent = insuranceAgentRepo.save(currentAgent);
			return insuranceAgentMapper.toDto(updatedAgent);
		}
		throw new InsuranceAgentNotFoundException("Insurance Agent with ID " + id + " not found");
	}

	@Override
	@Transactional
	public void deleteInsuranceAgent(Integer id) throws InsuranceAgentNotFoundException {
		if (insuranceAgentRepo.existsById(id)) {
			insuranceAgentRepo.deleteById(id);
		} else {
			throw new InsuranceAgentNotFoundException("Insurance Agent with ID " + id + " not found");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<InsuranceAgentResponse> findByStatus(AccountStatus status) {
		List<InsuranceAgent> agents = insuranceAgentRepo.findByStatus(status);
		return agents.stream()
				.map(insuranceAgentMapper::toDto)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<InsuranceAgentResponse> findByAgentId(Integer id) {
		List<InsuranceAgent> agents = insuranceAgentRepo.findByUserId(id);
		return agents.stream()
				.map(insuranceAgentMapper::toDto)
				.collect(Collectors.toList());
	}
}
