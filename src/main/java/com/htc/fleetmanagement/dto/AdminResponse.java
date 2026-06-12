package com.htc.fleetmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import com.htc.fleetmanagement.util.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminResponse {
    
    @NotNull(message = "Admin ID cannot be null")
    @Positive(message = "Admin ID must be positive")
    private Integer adminId;
    
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 255, message = "Username must be between 3 and 255 characters")
    private String username;
    
    @NotNull(message = "Role cannot be null")
    private Role role;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
}
