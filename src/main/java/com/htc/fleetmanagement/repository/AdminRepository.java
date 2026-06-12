package com.htc.fleetmanagement.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.htc.fleetmanagement.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    
	// Method to find an admin by username
    @Query("SELECT a FROM Admin a WHERE a.username = :username")
    Optional<Admin> findByUsername(@Param("username") String username);
    
}
