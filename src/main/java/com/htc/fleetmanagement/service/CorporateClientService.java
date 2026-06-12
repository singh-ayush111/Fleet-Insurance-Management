package com.htc.fleetmanagement.service;

import java.util.List;

import com.htc.fleetmanagement.dto.CorporateClientRequest;
import com.htc.fleetmanagement.dto.CorporateClientResponse;
import com.htc.fleetmanagement.exception.CorporateClientNotFoundException;

public interface CorporateClientService {
	CorporateClientResponse registerNewClient(CorporateClientRequest request);
	
	CorporateClientResponse findById(Integer id) throws CorporateClientNotFoundException;
	
	List<CorporateClientResponse> findAll();
	
	CorporateClientResponse updateClient(Integer id, CorporateClientRequest request) throws CorporateClientNotFoundException;
	
	CorporateClientResponse partialUpdateClient(Integer id, CorporateClientRequest request) throws CorporateClientNotFoundException;
	
	void deleteClient(Integer id) throws CorporateClientNotFoundException;
}

