package com.htc.fleetmanagement.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyTableResponse {
    
    @NotBlank(message = "Master policy number cannot be blank")
    @Pattern(regexp = "^[A-Z]{3}[0-9]{4}$", message = "Master policy number format is invalid")
    private String masterPolicyNumber;
    
    @NotBlank(message = "Policy name cannot be blank")
    @Size(min = 2, max = 100, message = "Policy name must be between 2 and 100 characters")
    private String policyName;
    
    @NotNull(message = "Premium cannot be null")
    @DecimalMin(value = "0.01", message = "Premium must be greater than 0")
    private BigDecimal premium;
    
    @NotBlank(message = "Benefits cannot be blank")
    @Size(min = 5, max = 500, message = "Benefits description must be between 5 and 500 characters")
    private String benefits;
}
