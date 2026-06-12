package com.htc.fleetmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.htc.fleetmanagement.entity.User;


@Repository
public interface UserRepository  extends JpaRepository<User, Integer>{

	 Optional<User> findByUsername(String username);

}
