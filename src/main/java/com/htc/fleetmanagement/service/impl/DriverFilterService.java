package com.htc.fleetmanagement.service.impl;

import com.htc.fleetmanagement.entity.FleetManager;
import com.htc.fleetmanagement.repository.JdbcDriverFilterRepository;
import com.htc.fleetmanagement.repository.JdbcDriverFilterRepository.DriverRiskStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverFilterService {


    @Autowired
    private JdbcDriverFilterRepository jdbcDriverFilterRepository;

    // Cache the driver risk statistics for 10 minutes to improve performance
    @Cacheable(value = "driverRiskStatistics", key = "'allStatistics'")
    public List<DriverRiskStatistic> getDriverRiskStatistics(FleetManager currentManager) {
        return jdbcDriverFilterRepository.getDriverRiskStatistics();
    }
}