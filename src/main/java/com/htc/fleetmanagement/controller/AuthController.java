package com.htc.fleetmanagement.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.htc.fleetmanagement.dto.ApiResponse;
import com.htc.fleetmanagement.dto.LoginRequest;
import com.htc.fleetmanagement.controller.AuthController;
import com.htc.fleetmanagement.entity.Token;
import com.htc.fleetmanagement.entity.User;
import com.htc.fleetmanagement.exception.*;
import com.htc.fleetmanagement.repository.TokenRepository;
import com.htc.fleetmanagement.repository.UserRepository;
import com.htc.fleetmanagement.service.impl.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired	
	private TokenRepository tokenRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtService jwtService;
	

	private Logger log=LoggerFactory.getLogger(AuthController.class);

	//saves the generated token in the database with associated user and token metadata
	private Token saveToken(String jwtToken ,String username) throws UserNotFoundException {
		Optional<User> user = userRepository.findByUsername(username);
		
		Token token = new Token();
		log.info("received token: " + jwtToken);
		token.setJwttoken(jwtToken);
		if (user.isPresent()) {
			token.setUser(user.get());
		}
		token.setRevoked(false);
		token.setExpired(false);
		token.setCreatedAt(java.time.LocalDateTime.now());
		token.setExpiresAt(java.time.LocalDateTime.now().plusHours(1));
		return tokenRepository.save(token);
	}
	
	
	//revokes all existing valid tokens for the user by marking them as expired and revoked in the database
	private void revokeToken(String username) throws UserNotFoundException {
		Optional<User> userOpt = userRepository.findByUsername(username);
		
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			List<Token> validUserToken = tokenRepository.findAllValidTokensByUser(user.getUserId());
			if (!validUserToken.isEmpty()) {
				validUserToken.forEach(t -> {
					t.setExpired(true);
					t.setRevoked(true);
				});
				tokenRepository.saveAll(validUserToken);
			}
		}
	}
	

	
	//handles user login requests, authenticates the user, generates a JWT token, 
	//revokes any existing valid tokens, saves the new token in the database, and 
	//returns the token in the response
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<String>> authenticateUser(@RequestBody LoginRequest loginRequest) {
		log.info("User login attempt for email: {}", loginRequest.getUsername());
		ApiResponse<String> response = new ApiResponse<>();
		try {
			log.info("User login attempt for username: {}", loginRequest.getUsername());
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
			String jwtToken = jwtService.generateToken(authentication.getName());
             revokeToken(authentication.getName());
 			log.info("token: "+jwtToken);
             saveToken(jwtToken, authentication.getName());
			response.setStatusCode(HttpStatus.OK.value());
			response.setMessage("Login successful");
			response.setData(jwtToken);
			log.info("User logged in successfully for username: {}", loginRequest.getUsername());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (AuthenticationException e) {
			log.error("Login failed for email: {}", loginRequest.getUsername(), e);

			response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
			response.setMessage("Invalid email or password");
			response.setData(null);

			return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
		} catch (UserNotFoundException e) {
			log.error("User not found for email: {}", loginRequest.getUsername(), e);

			response.setStatusCode(HttpStatus.NOT_FOUND.value());
			response.setMessage("User not found");
			response.setData(null);

			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	//handles user logout requests by extracting the JWT token from the Authorization header,
	//finding the corresponding token in the database, marking it as expired and revoked,
	//and returning a response indicating the logout status
	@PostMapping("/logout")
	public ResponseEntity<ApiResponse<String>> logoutUser(@org.springframework.web.bind.annotation.RequestHeader(value = "Authorization", required = false) String authHeader) {
		ApiResponse<String> response = new ApiResponse<>();
		try {
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				String token = authHeader.substring(7);
				var tokenEntity = tokenRepository.findByJwttoken(token);
				
				if (tokenEntity.isPresent()) {
					Token t = tokenEntity.get();
					t.setExpired(true);
					t.setRevoked(true);
					tokenRepository.save(t);
					
					log.info("Token revoked successfully for user");
					response.setStatusCode(HttpStatus.OK.value());
					response.setMessage("Logout successful");
					response.setData(null);
					return new ResponseEntity<>(response, HttpStatus.OK);
				} else {
					response.setStatusCode(HttpStatus.BAD_REQUEST.value());
					response.setMessage("Invalid token");
					response.setData(null);
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				}
			} else {
				response.setStatusCode(HttpStatus.BAD_REQUEST.value());
				response.setMessage("Authorization header missing");
				response.setData(null);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			log.error("Logout failed: {}", e.getMessage(), e);
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setMessage("Logout failed");
			response.setData(null);
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}