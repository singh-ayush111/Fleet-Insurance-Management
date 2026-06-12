package com.htc.fleetmanagement.service.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.htc.fleetmanagement.entity.Driver;
import com.htc.fleetmanagement.entity.FleetClaim;
import com.htc.fleetmanagement.repository.DriverRepository;
import com.htc.fleetmanagement.repository.FleetClaimRepository;

import jakarta.transaction.Transactional;

@Component
public class RiskScore {

    @Autowired
    private DriverRepository driverRepo;

    @Autowired
    private FleetClaimRepository claimRepo;
    
    // Constants for risk score calculation
    private static final float DEFAULT_RISK_SCORE = 1.0f;
    private static final float MEDIUM_RISK_SCORE = 2.0f;
    private static final float HIGH_RISK_SCORE = 3.0f;
    private static final double WEIGHTED_SCORE_LOW_THRESHOLD = 3.0;
    private static final double WEIGHTED_SCORE_MEDIUM_THRESHOLD = 7.0;

    @Scheduled(cron = "0 0 2 * * *") 
    @Transactional
    public void recalculateAllRiskScores() {
        try {
            List<Driver> allDrivers = driverRepo.findAll();
            if (allDrivers == null || allDrivers.isEmpty()) {
                System.out.println("No drivers found for risk score recalculation");
                return;
            }
            allDrivers.forEach(this::updateDriverRiskScore);
            System.out.println("Successfully recalculated risk scores for " + allDrivers.size() + " drivers");
        } catch (Exception e) {
            System.err.println("Error during scheduled risk score recalculation: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to recalculate all risk scores", e);
        }
    }

    public void updateDriverRiskScore(Driver driver) {
	    try {
	        if (driver == null) {
	            throw new IllegalArgumentException("Driver cannot be null");
	        }

	        List<FleetClaim> claims = claimRepo.findByDriver(driver);

	        if (claims.isEmpty()) {
	            driver.setRiskScore(DEFAULT_RISK_SCORE);
	            driverRepo.save(driver);
	            return;
	        }

	        LocalDate today = LocalDate.now();
	        double weightedScore = 0.0;

	        for (FleetClaim claim : claims) {
	            if (claim == null || claim.getIncidentDate() == null || claim.getRepairCost() == null) {
	                continue;
	            }

	            // Calculate weighted score
	            long monthsAgo = ChronoUnit.MONTHS.between(claim.getIncidentDate(), today);
	            double recencyWeight = calculateRecencyWeight(monthsAgo);
	            
	            double cost = claim.getRepairCost().doubleValue();
	            double severityWeight = calculateSeverityWeight(cost);

	            weightedScore += recencyWeight * severityWeight;
	        }

	        // Map weighted score to 1.0 - 3.0
	        float riskScore = calculateRiskScore(weightedScore);

	        driver.setRiskScore(riskScore);
	        driverRepo.save(driver);
	    } catch (IllegalArgumentException e) {
	        System.err.println("Invalid argument in updateDriverRiskScore: " + e.getMessage());
	        throw e;
	    } catch (Exception e) {
	        System.err.println("Error updating driver risk score: " + e.getMessage());
	        e.printStackTrace();
	        throw new RuntimeException("Failed to update risk score for driver", e);
	    }
	}

	//Calculate recency weight based on months since incident
	//Weight 1: how recent the claim is

	private double calculateRecencyWeight(long monthsAgo) {
	    int recencyCategory = categorizeRecency(monthsAgo);
	    switch (recencyCategory) {
	        case 0: // Recent (0-12 months)
	            return 1.0;
	        case 1: // Moderate (13-24 months)
	            return 0.5;
	        case 2: // Old (25+ months)
	            return 0.25;
	        default:
	            return 0.0;
	    }
	}

	//Categorize recency based on months

	private int categorizeRecency(long monthsAgo) {
	    if (monthsAgo <= 12) {
	        return 0;
	    } else if (monthsAgo <= 24) {
	        return 1;
	    } else {
	        return 2;
	    }
	}

	//Calculate severity weight based on repair cost
	//Weight 2: cost of the repair

	private double calculateSeverityWeight(double cost) {
	    int costCategory = categorizeCost(cost);
	    switch (costCategory) {
	        case 0: // Low cost (<5000)
	            return 1.0;
	        case 1: // Medium cost (5000-20000)
	            return 1.5;
	        case 2: // High cost (>20000)
	            return 2.0;
	        default:
	            return 1.0;
	    }
	}

	//Categorize cost into ranges
	private int categorizeCost(double cost) {
	    if (cost < 5000) {
	        return 0;
	    } else if (cost <= 20000) {
	        return 1;
	    } else {
	        return 2;
	    }
	}

	//Calculate final risk score from weighted score
	private float calculateRiskScore(double weightedScore) {
	    int scoreCategory = categorizeWeightedScore(weightedScore);
	    switch (scoreCategory) {
	        case 0: // Low risk
	            return DEFAULT_RISK_SCORE;
	        case 1: // Medium risk
	            return MEDIUM_RISK_SCORE;
	        case 2: // High risk
	            return HIGH_RISK_SCORE;
	        default:
	            return DEFAULT_RISK_SCORE;
	    }
	}

	//Categorize weighted score into risk levels
	private int categorizeWeightedScore(double weightedScore) {
	    if (weightedScore < WEIGHTED_SCORE_LOW_THRESHOLD) {
	        return 0;
	    } else if (weightedScore <= WEIGHTED_SCORE_MEDIUM_THRESHOLD) {
	        return 1;
	    } else {
	        return 2;
	    }
	}
}