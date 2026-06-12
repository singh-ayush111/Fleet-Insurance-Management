package com.htc.fleetmanagement.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.htc.fleetmanagement.entity.User;
import com.htc.fleetmanagement.exception.UserNotFoundException;
import com.htc.fleetmanagement.repository.UserRepository;
import com.htc.fleetmanagement.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public User registerUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword())); 
		return userRepo.save(user);
	}

	@Override
	public User findById(Integer id) throws UserNotFoundException {
		Optional<User> user = userRepo.findById(id);
		if (user.isPresent()) {
			return user.get();
		}
		throw new UserNotFoundException("User with ID " + id + " not found");
	}

	@Override
	public List<User> findAll() {
		return userRepo.findAll();
	}

	@Override
	public User updateUser(User user) throws UserNotFoundException {
		if (user.getUserId() != null && userRepo.existsById(user.getUserId())) {
			return userRepo.save(user);
		}
		throw new UserNotFoundException("User with ID " + user.getUserId() + " not found");
	}

	@Override
	public void deleteUser(Integer id) throws UserNotFoundException {
		if (userRepo.existsById(id)) {
			userRepo.deleteById(id);
		} else {
			throw new UserNotFoundException("User with ID " + id + " not found");
		}
	}
}
