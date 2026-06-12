package com.htc.fleetmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.htc.fleetmanagement.entity.User;
import com.htc.fleetmanagement.exception.UserNotFoundException;
import com.htc.fleetmanagement.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<User> registerUser(@RequestBody User user) {
		User savedUser = userService.registerUser(user);
		return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable Integer id) throws UserNotFoundException {
		User user = userService.findById(id);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> users = userService.findAll();
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User user) throws UserNotFoundException {
		user.setUserId(id);
		User updatedUser = userService.updateUser(user);
		return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<User> partialUpdateUser(@PathVariable Integer id, @RequestBody User user) throws UserNotFoundException {
		User currentUser = userService.findById(id);
		
		if (user.getUsername() != null) {
			currentUser.setUsername(user.getUsername());
		}
		if (user.getPassword() != null) {
			currentUser.setPassword(user.getPassword());
		}
		if (user.getRole() != null) {
			currentUser.setRole(user.getRole());
		}
		
		User updatedUser = userService.updateUser(currentUser);
		return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Integer id) throws UserNotFoundException {
		userService.deleteUser(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
