package com.htc.fleetmanagement.mapper;

import com.htc.fleetmanagement.dto.EmployeeRequest;
import com.htc.fleetmanagement.dto.EmployeeResponse;
import com.htc.fleetmanagement.entity.Employee;
import com.htc.fleetmanagement.entity.CorporateClient;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {
    
    public EmployeeResponse toDto(Employee employee) {
        if (employee == null) {
            return null;
        }
        
        EmployeeResponse response = new EmployeeResponse();
        response.setEmployeeId(employee.getEmployeeId());
        response.setEmail(employee.getEmail());
        response.setName(employee.getName());
        response.setLicenseNumber(employee.getLicenseNumber());
        response.setCreatedAt(employee.getCreatedAt());
        response.setUpdatedAt(employee.getUpdatedAt());
        
        if (employee.getClient() != null) {
            response.setClientId(employee.getClient().getUserId());
            response.setClientName(employee.getClient().getCompanyName());
        }
        
        return response;
    }
    
    public Employee toEntity(EmployeeRequest request) {
        if (request == null) {
            return null;
        }
        
        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setLicenseNumber(request.getLicenseNumber());
        employee.setEmail(request.getEmail());
        employee.setPassword(request.getPassword());
        
        if (request.getClientId() != null) {
            CorporateClient client = new CorporateClient();
            client.setUserId(request.getClientId());
            employee.setClient(client);
        }
        
        return employee;
    }
}
