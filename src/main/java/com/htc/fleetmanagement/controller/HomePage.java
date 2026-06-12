package com.htc.fleetmanagement.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class HomePage {
	
	// This endpoint is just for testing purposes to verify that the application is running and to check session management.
	@GetMapping("/")
	public String Home(HttpServletRequest request) {
		return "Home Page"+request.getSession().getId();
	}
	
	// This endpoint is used to retrieve the CSRF token for the current session
	@GetMapping("/csrftoken")
	public CsrfToken getCsrfToken(HttpServletRequest request) {
		return (CsrfToken) request.getAttribute("_csrf");
	}

}
