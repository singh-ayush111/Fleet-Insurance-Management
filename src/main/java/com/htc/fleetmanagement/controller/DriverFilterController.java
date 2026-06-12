package com.htc.fleetmanagement.controller;

import com.htc.fleetmanagement.dto.DriverFilterResponse;
import com.htc.fleetmanagement.entity.FleetManager;
import com.htc.fleetmanagement.repository.JdbcDriverFilterRepository.DriverRiskStatistic;
import com.htc.fleetmanagement.service.impl.DriverFilterService;
import com.htc.fleetmanagement.util.AuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/drivers/filter")
public class DriverFilterController {

    @Autowired
    private DriverFilterService driverFilterService;

    @Autowired
    private AuthenticationUtil authenticationUtil;

    // Endpoint to retrieve driver risk statistics for the current fleet manager's client
    @GetMapping("/statistics/risk")
    public ResponseEntity<DriverFilterResponse<List<DriverRiskStatistic>>> getRiskStatistics() {

        FleetManager currentManager = authenticationUtil.getCurrentFleetManager();
        List<DriverRiskStatistic> statistics = driverFilterService.getDriverRiskStatistics(currentManager);

        DriverFilterResponse<List<DriverRiskStatistic>> response = new DriverFilterResponse<>();
        response.setSuccess(true);
        response.setMessage("Driver risk statistics retrieved for client ID: " + currentManager.getClient().getUserId());
        response.setData(statistics);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}