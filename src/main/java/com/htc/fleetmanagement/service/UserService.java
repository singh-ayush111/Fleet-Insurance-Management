package com.htc.fleetmanagement.service;

import java.util.List;

import com.htc.fleetmanagement.entity.User;
import com.htc.fleetmanagement.exception.UserNotFoundException;

public interface UserService {
	User registerUser(User user);
	
	User findById(Integer id) throws UserNotFoundException;
	
	List<User> findAll();
	
	User updateUser(User user) throws UserNotFoundException;
	
	void deleteUser(Integer id) throws UserNotFoundException;
}
