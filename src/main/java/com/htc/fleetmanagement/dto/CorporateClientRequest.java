package com.htc.fleetmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;

import com.htc.fleetmanagement.util.Role;
import com.htc.fleetmanagement.validator.MailValidator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CorporateClientRequest {
    
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    private String password;
    
    @NotBlank(message = "Company name cannot be blank")
    @Size(min = 2, max = 100, message = "Company name must be between 2 and 100 characters")
    private String companyName;
    
    @NotBlank(message = "Contact email cannot be blank")
    @MailValidator(message = "Contact email should be valid")
    private String contactEmail;
    
    @NotNull(message = "Role cannot be null")
    private Role role;
    
    @NotNull(message = "Master policy cannot be blank")
    @Valid
    private MasterPolicyRequest masterPolicy;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MasterPolicyRequest {
        @NotBlank(message = "Master policy number cannot be blank")
        @Pattern(regexp = "^[A-Z]{3}[0-9]{4}$", message = "Master policy number format is invalid")
        private String masterPolicyNumber;
    }
}
