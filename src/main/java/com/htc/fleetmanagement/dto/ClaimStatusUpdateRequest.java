package com.htc.fleetmanagement.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.htc.fleetmanagement.util.ClaimStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimStatusUpdateRequest {
    
    @NotNull(message = "Status cannot be null")
    private ClaimStatus status;
}
