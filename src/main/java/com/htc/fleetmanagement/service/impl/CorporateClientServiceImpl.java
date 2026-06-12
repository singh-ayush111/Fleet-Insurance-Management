package com.htc.fleetmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.htc.fleetmanagement.dto.CorporateClientRequest;
import com.htc.fleetmanagement.dto.CorporateClientResponse;
import com.htc.fleetmanagement.entity.CorporateClient;
import com.htc.fleetmanagement.exception.CorporateClientNotFoundException;
import com.htc.fleetmanagement.mapper.CorporateClientMapper;
import com.htc.fleetmanagement.repository.CorporateClientRepository;
import com.htc.fleetmanagement.service.CorporateClientService;


@Service
public class CorporateClientServiceImpl implements CorporateClientService {

	// Constants for error messages
	private static final String CLIENT_NOT_FOUND_MSG = "Corporate Client with ID %d not found";
	private static final String CACHE_KEY = "corporateClients";
	private static final String ALL_CLIENTS_KEY = "allClients";

	@Autowired
	private CorporateClientRepository corprepo;

	@Autowired
	private CorporateClientMapper corporateClientMapper;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	@Transactional
	@CacheEvict(value = CACHE_KEY, allEntries = true)
	public CorporateClientResponse registerNewClient(CorporateClientRequest request) {
		try {
			if (request == null) {
				throw new IllegalArgumentException("Client request cannot be null");
			}

			CorporateClient client = corporateClientMapper.toEntity(request);
			client.setUserId(null);
			client.setPassword(passwordEncoder.encode(client.getPassword()));
			CorporateClient savedClient = corprepo.save(client);
			
			System.out.println("Successfully registered new client: " + savedClient.getCompanyName());
			return corporateClientMapper.toDto(savedClient);
		} catch (IllegalArgumentException e) {
			System.err.println("Invalid request for client registration: " + e.getMessage());
			throw e;
		} catch (Exception e) {
			System.err.println("Error registering new client: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Failed to register new corporate client", e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = CACHE_KEY, key = "#id", unless = "#result == null")
	public CorporateClientResponse findById(Integer id) throws CorporateClientNotFoundException {
		try {
			if (id == null || id <= 0) {
				throw new IllegalArgumentException("Client ID must be a positive number");
			}

			CorporateClient client = findClientOrThrow(id);
			return corporateClientMapper.toDto(client);
		} catch (CorporateClientNotFoundException e) {
			throw e;
		} catch (IllegalArgumentException e) {
			System.err.println("Invalid argument in findById: " + e.getMessage());
			throw e;
		} catch (Exception e) {
			System.err.println("Error finding client by ID: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Failed to find corporate client", e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = CACHE_KEY, key = "'" + ALL_CLIENTS_KEY + "'")
	public List<CorporateClientResponse> findAll() {
		try {
			List<CorporateClient> clients = corprepo.findAll();
			
			if (clients == null || clients.isEmpty()) {
				System.out.println("No corporate clients found");
				return List.of();
			}
			
			List<CorporateClientResponse> responses = clients.stream()
					.map(corporateClientMapper::toDto)
					.collect(Collectors.toList());
			
			System.out.println("Found " + responses.size() + " corporate clients");
			return responses;
		} catch (Exception e) {
			System.err.println("Error retrieving all clients: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Failed to retrieve all corporate clients", e);
		}
	}

	@Override
	@Transactional
	@CacheEvict(value = CACHE_KEY, allEntries = true)
	public CorporateClientResponse updateClient(Integer id, CorporateClientRequest request) 
			throws CorporateClientNotFoundException {
		try {
			if (id == null || id <= 0) {
				throw new IllegalArgumentException("Client ID must be a positive number");
			}
			if (request == null) {
				throw new IllegalArgumentException("Client request cannot be null");
			}

			// Verify client exists
			findClientOrThrow(id);

			CorporateClient client = corporateClientMapper.toEntity(request);
			client.setUserId(id);
			CorporateClient updatedClient = corprepo.save(client);
			
			System.out.println("Successfully updated client with ID: " + id);
			return corporateClientMapper.toDto(updatedClient);
		} catch (CorporateClientNotFoundException e) {
			throw e;
		} catch (IllegalArgumentException e) {
			System.err.println("Invalid argument in updateClient: " + e.getMessage());
			throw e;
		} catch (Exception e) {
			System.err.println("Error updating client with ID " + id + ": " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Failed to update corporate client", e);
		}
	}

	@Override
	@Transactional
	@CacheEvict(value = CACHE_KEY, allEntries = true)
	public CorporateClientResponse partialUpdateClient(Integer id, CorporateClientRequest request) 
			throws CorporateClientNotFoundException {
		try {
			if (id == null || id <= 0) {
				throw new IllegalArgumentException("Client ID must be a positive number");
			}
			if (request == null) {
				throw new IllegalArgumentException("Client request cannot be null");
			}

			CorporateClient currentClient = findClientOrThrow(id);

			// Update fields if provided (non-null)
			updateClientFields(currentClient, request);

			CorporateClient updatedClient = corprepo.save(currentClient);
			
			System.out.println("Successfully partially updated client with ID: " + id);
			return corporateClientMapper.toDto(updatedClient);
		} catch (CorporateClientNotFoundException e) {
			throw e;
		} catch (IllegalArgumentException e) {
			System.err.println("Invalid argument in partialUpdateClient: " + e.getMessage());
			throw e;
		} catch (Exception e) {
			System.err.println("Error partially updating client with ID " + id + ": " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Failed to partially update corporate client", e);
		}
	}

	@Override
	@Transactional
	@CacheEvict(value = CACHE_KEY, allEntries = true)
	public void deleteClient(Integer id) throws CorporateClientNotFoundException {
		try {
			if (id == null || id <= 0) {
				throw new IllegalArgumentException("Client ID must be a positive number");
			}

			// Verify client exists before deletion
			findClientOrThrow(id);

			corprepo.deleteById(id);
			System.out.println("Successfully deleted client with ID: " + id);
		} catch (CorporateClientNotFoundException e) {
			throw e;
		} catch (IllegalArgumentException e) {
			System.err.println("Invalid argument in deleteClient: " + e.getMessage());
			throw e;
		} catch (Exception e) {
			System.err.println("Error deleting client with ID " + id + ": " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Failed to delete corporate client", e);
		}
	}

	/**
	 * Helper method to find a client by ID or throw CorporateClientNotFoundException
	 * 
	 * @param id the client ID to find
	 * @return the CorporateClient if found
	 * @throws CorporateClientNotFoundException if client not found
	 */
	private CorporateClient findClientOrThrow(Integer id) throws CorporateClientNotFoundException {
		Optional<CorporateClient> client = corprepo.findById(id);
		if (!client.isPresent()) {
			String errorMsg = String.format(CLIENT_NOT_FOUND_MSG, id);
			throw new CorporateClientNotFoundException(errorMsg);
		}
		return client.get();
	}

	/**
	 * Helper method to update client fields from request
	 * Only updates fields that are non-null in the request
	 * 
	 * @param currentClient the existing client to update
	 * @param request the request containing new values
	 */
	private void updateClientFields(CorporateClient currentClient, CorporateClientRequest request) {
		if (request.getCompanyName() != null && !request.getCompanyName().trim().isEmpty()) {
			currentClient.setCompanyName(request.getCompanyName());
		}

		if (request.getContactEmail() != null && !request.getContactEmail().trim().isEmpty()) {
			currentClient.setContactEmail(request.getContactEmail());
		}
	}
}


