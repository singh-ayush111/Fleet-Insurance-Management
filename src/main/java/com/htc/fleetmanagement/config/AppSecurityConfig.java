package com.htc.fleetmanagement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.htc.fleetmanagement.filter.JwtAuthenticationFilter;
import com.htc.fleetmanagement.service.impl.CustomUserDetailService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class AppSecurityConfig {
	
	@Autowired
	private CustomUserDetailService customUserDetailService;
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	
	//allows access to Swagger UI and API documentation without authentication
	private static final String[] SWAGGER_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
    };
	
	
	// Bean for password encoding using BCrypt
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	
	//authentication provider that uses the custom user details service and password encoder
	@Bean
	public AuthenticationProvider authenticationProvider() {
	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(customUserDetailService);
	    authProvider.setPasswordEncoder(bCryptPasswordEncoder());
	    return authProvider;
	}

	
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(request -> request
                		.requestMatchers(SWAGGER_WHITELIST).permitAll()
                		.requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                		.requestMatchers( "/api/policies/**").hasAnyRole("INSURANCE_AGENT", "ADMIN")
                		.requestMatchers( "/api/corporate-clients/**").hasAnyRole("INSURANCE_AGENT", "ADMIN")
                		.requestMatchers( "/api/fleet-claims/**").hasAnyRole("DRIVER", "FLEET_MANAGER", "ADMIN")
                		.requestMatchers("/api/fleet-vehicles/**").hasAnyRole("FLEET_MANAGER", "ADMIN")
                		.requestMatchers("/api/fleet-managers/**").hasAnyRole("CORPORATE_CLIENT", "ADMIN")
                		.requestMatchers( "/api/drivers/**").hasAnyRole("FLEET_MANAGER", "CORPORATE_CLIENT", "ADMIN")
                		.requestMatchers( "/api/admins/**").hasRole("ADMIN")
                		.requestMatchers( "/api/v1/drivers/filter/**").permitAll()
                		.requestMatchers( "/api/insurance-agents/**").hasAnyRole("ADMIN","INSURANCE_AGENT")
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	
	
	// Exposes the AuthenticationManager as a bean to be used in other parts of the application, such as in authentication controllers
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}