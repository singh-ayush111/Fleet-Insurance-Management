package com.htc.fleetmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;

import com.htc.fleetmanagement.validator.MailValidator;
import com.htc.fleetmanagement.util.Role;

import jakarta.validation.constraints.DecimalMax;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverRequest {
    
    @NotNull(message = "Client ID cannot be null")
    @Positive(message = "Client ID must be positive")
    private Integer clientId;
    
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    
    @NotBlank(message = "License number cannot be blank")
    @Pattern(regexp = "^[A-Z0-9]{6,20}$", message = "License number format is invalid")
    private String licenseNumber;
    
    @NotNull(message = "Risk score cannot be null")
    @DecimalMin(value = "0.0", message = "Risk score must be non-negative")
    @DecimalMax(value = "3.0", message = "Risk score must not exceed 3.0")
    private Float riskScore;
    
	@NotNull(message = "Role cannot be null")
	private Role role;
	
    @NotBlank(message = "Contact email cannot be blank")
    @MailValidator(message = "Contact email should be valid")
    private String Email;
    
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
	
	@NotBlank(message = "Password cannot be blank")
	@Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
	private String password;
    
}
