package com.htc.fleetmanagement.repository;

import com.htc.fleetmanagement.entity.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Repository
public class JdbcDriverFilterRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

   
    public List<DriverRiskStatistic> getDriverRiskStatistics() {
        String sql = "SELECT " +
                     "CASE " +
                     "  WHEN d.risk_score < 2.0 THEN 'Low Risk' " +
                     "  WHEN d.risk_score < 4.0 THEN 'Medium Risk' " +
                     "  WHEN d.risk_score < 6.0 THEN 'High Risk' " +
                     "  ELSE 'Very High Risk' " +
                     "END as risk_category, " +
                     "COUNT(*) as driver_count, " +
                     "AVG(d.risk_score) as avg_risk_score " +
                     "FROM 351_employee_351 e " +
                     "JOIN 351_driver_351 d ON e.employee_id = d.driver_id " +
                     "GROUP BY risk_category " +
                     "ORDER BY avg_risk_score ASC";
        
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            DriverRiskStatistic stat = new DriverRiskStatistic();
            stat.setRiskCategory(rs.getString("risk_category"));
            stat.setDriverCount(rs.getInt("driver_count"));
            stat.setAverageRiskScore(rs.getFloat("avg_risk_score"));
            return stat;
        });
    }



    
    public static class DriverRiskStatistic {
        private String riskCategory;
        private int driverCount;
        private float averageRiskScore;

        public DriverRiskStatistic() {}

        public String getRiskCategory() {
            return riskCategory;
        }

        public void setRiskCategory(String riskCategory) {
            this.riskCategory = riskCategory;
        }

        public int getDriverCount() {
            return driverCount;
        }

        public void setDriverCount(int driverCount) {
            this.driverCount = driverCount;
        }

        public float getAverageRiskScore() {
            return averageRiskScore;
        }

        public void setAverageRiskScore(float averageRiskScore) {
            this.averageRiskScore = averageRiskScore;
        }

        @Override
        public String toString() {
            return "DriverRiskStatistic{" +
                    "riskCategory='" + riskCategory + '\'' +
                    ", driverCount=" + driverCount +
                    ", averageRiskScore=" + averageRiskScore +
                    '}';
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    private final RowMapper<Driver> driverRowMapper = new RowMapper<Driver>() {
        @Override
        public Driver mapRow(ResultSet rs, int rowNum) throws SQLException {
            Driver driver = new Driver();
            driver.setEmployeeId(rs.getInt("employee_id"));
            driver.setName(rs.getString("name"));
            driver.setEmail(rs.getString("email"));
            driver.setUsername(rs.getString("username"));
            driver.setLicenseNumber(rs.getString("license_number"));
            driver.setRiskScore(rs.getFloat("risk_score"));
            return driver;
        }
    };
}
