package com.htc.fleetmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import com.htc.fleetmanagement.validator.MailValidator;
import com.htc.fleetmanagement.util.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CorporateClientResponse {
    
    @NotNull(message = "User ID cannot be null")
    @Positive(message = "User ID must be positive")
    private Integer userId;
    
    @NotBlank(message = "Username cannot be blank")
    private String username;
    
    @NotNull(message = "Role cannot be null")
    private Role role;
    
    @NotBlank(message = "Company name cannot be blank")
    private String companyName;
    
    @NotBlank(message = "Contact email cannot be blank")
    @MailValidator(message = "Contact email should be valid")
    private String contactEmail;
    
    @NotBlank(message = "Master policy number cannot be blank")
    @Pattern(regexp = "^[A-Z]{3}[0-9]{4}$", message = "Master policy number format is invalid")
    private String masterPolicyNumber;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private Integer vehicleCount;
    
    private BigDecimal totalPremium;
}
