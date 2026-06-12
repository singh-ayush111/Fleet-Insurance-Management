package com.htc.fleetmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for Driver Filtering Criteria
 * Used by fleet managers to apply multiple filter conditions
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DriverFilterCriteria {
    
    private Integer clientId;
    
    private Integer managerId;
    
    private Float minRiskScore;
    
    private Float maxRiskScore;
    
    private String namePattern;
    
    @Override
    public String toString() {
        return "DriverFilterCriteria{" +
                "clientId=" + clientId +
                ", managerId=" + managerId +
                ", minRiskScore=" + minRiskScore +
                ", maxRiskScore=" + maxRiskScore +
                ", namePattern='" + namePattern + '\'' +
                '}';
    }
}
