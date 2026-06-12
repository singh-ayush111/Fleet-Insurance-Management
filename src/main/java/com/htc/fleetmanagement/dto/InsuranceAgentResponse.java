package com.htc.fleetmanagement.dto;

import com.htc.fleetmanagement.util.AccountStatus;
import com.htc.fleetmanagement.util.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceAgentResponse {
    
    @NotNull(message = "User ID cannot be null")
    @Positive(message = "User ID must be positive")
    private Integer userId;
    
    @NotBlank(message = "Username cannot be blank")
    private String username;
    
    @NotNull(message = "Role cannot be null")
    private Role role;
    
    @NotNull(message = "Status cannot be null")
    private AccountStatus status;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
