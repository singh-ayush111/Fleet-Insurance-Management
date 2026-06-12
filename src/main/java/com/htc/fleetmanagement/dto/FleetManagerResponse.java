package com.htc.fleetmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import com.htc.fleetmanagement.validator.MailValidator;
import com.htc.fleetmanagement.util.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FleetManagerResponse {
    
    @NotNull(message = "Employee ID cannot be null")
    @Positive(message = "Employee ID must be positive")
    private Integer employeeId;
    
    @NotNull(message = "Client ID cannot be null")
    @Positive(message = "Client ID must be positive")
    private Integer clientId;
    
    @NotBlank(message = "Client name cannot be blank")
    private String clientName;
    
    @NotBlank(message = "Name cannot be blank")
    private String name;
    
    @NotBlank(message = "License number cannot be blank")
    @Pattern(regexp = "^[A-Z0-9]{6,20}$", message = "License number format is invalid")
    private String licenseNumber;
    
    @NotNull(message = "Role cannot be null")
    private Role role;
    
    @NotBlank(message = "Contact email cannot be blank")
    @MailValidator(message = "Contact email should be valid")
    private String Email;
    
    @NotBlank(message = "Username cannot be blank")
    private String username;
    
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
