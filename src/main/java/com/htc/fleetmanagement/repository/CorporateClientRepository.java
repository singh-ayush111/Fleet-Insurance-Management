package com.htc.fleetmanagement.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.htc.fleetmanagement.entity.CorporateClient;

public interface CorporateClientRepository  extends JpaRepository<CorporateClient, Integer>{
	
	// Find a corporate client by username
	@Query("SELECT c FROM CorporateClient c WHERE c.username = :username")
	Optional<CorporateClient> findByUsername(@Param("username") String username);

}
