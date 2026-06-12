package com.htc.fleetmanagement.dto;

import com.htc.fleetmanagement.util.AccountStatus;
import com.htc.fleetmanagement.util.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceAgentRequest {
    
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    private String password;   
    
    @NotNull(message = "Role cannot be null")
    private Role role;
    
    @NotNull(message = "Status cannot be null")
    private AccountStatus status;
}
