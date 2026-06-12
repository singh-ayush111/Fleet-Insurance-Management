package com.htc.fleetmanagement.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.htc.fleetmanagement.entity.Driver;
import com.htc.fleetmanagement.entity.Employee;
import com.htc.fleetmanagement.entity.FleetManager;
import com.htc.fleetmanagement.entity.User;
import com.htc.fleetmanagement.exception.UnauthorizedAccessException;
import com.htc.fleetmanagement.repository.DriverRepository;
import com.htc.fleetmanagement.repository.EmployeeRepository;
import com.htc.fleetmanagement.repository.FleetManagerRepository;
import com.htc.fleetmanagement.repository.UserRepository;


@Component
public class AuthenticationUtil {
    
    // Enum for user type detection
    private enum UserTypeDetected {
        USER,
        DRIVER,
        FLEET_MANAGER,
        EMPLOYEE,
        NOT_FOUND
    }
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DriverRepository driverRepository;
    
    @Autowired
    private FleetManagerRepository fleetManagerRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    /**
     * Get the currently authenticated user as a UserDetails object
     * Searches through all user repositories in priority order
     * 
     * @return UserDetails object representing the authenticated user
     * @throws UnauthorizedAccessException if user is not authenticated or not found
     */
    public UserDetails getCurrentUser() {
        try {
            Authentication authentication = getAuthenticationOrThrow();
            String username = authentication.getName();
            
            UserTypeDetected userType = detectUserType(username);
            
            switch (userType) {
                case USER:
                    User user = userRepository.findByUsername(username).orElse(null);
                    if (user != null) {
                        return user;
                    }
                    break;
                case DRIVER:
                    Driver driver = driverRepository.findByUsername(username).orElse(null);
                    if (driver != null) {
                        return driver;
                    }
                    break;
                case FLEET_MANAGER:
                    FleetManager fleetManager = fleetManagerRepository.findByUsername(username).orElse(null);
                    if (fleetManager != null) {
                        return fleetManager;
                    }
                    break;
                case EMPLOYEE:
                    Employee employee = employeeRepository.findByUsername(username).orElse(null);
                    if (employee != null) {
                        return employee;
                    }
                    break;
                case NOT_FOUND:
                default:
                    throw new UnauthorizedAccessException("User not found: " + username);
            }
            
            throw new UnauthorizedAccessException("User not found: " + username);
        } catch (UnauthorizedAccessException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Error getting current user: " + e.getMessage());
            e.printStackTrace();
            throw new UnauthorizedAccessException("Failed to retrieve current user", e);
        }
    }
    
    /**
     * Get the currently authenticated user as a Driver object
     * 
     * @return Driver object representing the authenticated driver
     * @throws UnauthorizedAccessException if user is not authenticated or not a driver
     */
    public Driver getCurrentDriver() {
        try {
            Authentication authentication = getAuthenticationOrThrow();
            String username = authentication.getName();
            
            Driver driver = driverRepository.findByUsername(username).orElse(null);
            if (driver != null) {
                return driver;
            }
            
            throw new UnauthorizedAccessException("Driver not found for user: " + username);
        } catch (UnauthorizedAccessException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Error getting current driver: " + e.getMessage());
            e.printStackTrace();
            throw new UnauthorizedAccessException("Failed to retrieve current driver", e);
        }
    }
    
    /**
     * Check if current user is a driver
     * 
     * @return true if the user is a driver, false otherwise
     */
    public boolean isCurrentUserDriver() {
        try {
            UserDetails currentUser = getCurrentUser();
            
            switch (detectObjectType(currentUser)) {
                case DRIVER:
                    Driver driver = (Driver) currentUser;
                    return driver.getRole() != null && driver.getRole().equals("DRIVER");
                case USER:
                    User user = (User) currentUser;
                    return user.getRole() != null && user.getRole().equals("DRIVER");
                default:
                    return false;
            }
        } catch (UnauthorizedAccessException e) {
            return false;
        }
    }
    
    /**
     * Get the currently authenticated user as a FleetManager object
     * 
     * @return FleetManager object representing the authenticated fleet manager
     * @throws UnauthorizedAccessException if user is not authenticated or not a fleet manager
     */
    public FleetManager getCurrentFleetManager() {
        try {
            Authentication authentication = getAuthenticationOrThrow();
            String username = authentication.getName();
            
            FleetManager fleetManager = fleetManagerRepository.findByUsername(username).orElse(null);
            if (fleetManager != null) {
                return fleetManager;
            }
            
            throw new UnauthorizedAccessException("Fleet Manager not found for user: " + username);
        } catch (UnauthorizedAccessException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Error getting current fleet manager: " + e.getMessage());
            e.printStackTrace();
            throw new UnauthorizedAccessException("Failed to retrieve current fleet manager", e);
        }
    }
    
    /**
     * Check if current user is a fleet manager
     * 
     * @return true if the user is a fleet manager, false otherwise
     */
    public boolean isCurrentUserFleetManager() {
        try {
            getCurrentFleetManager();
            return true;
        } catch (UnauthorizedAccessException e) {
            return false;
        }
    }
    
    /**
     * Helper method to get authentication or throw exception
     * 
     * @return Authentication object if user is authenticated
     * @throws UnauthorizedAccessException if user is not authenticated
     */
    private Authentication getAuthenticationOrThrow() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedAccessException("User is not authenticated");
        }
        
        return authentication;
    }
    
    /**
     * Helper method to detect user type from username
     * Searches repositories in priority order
     * 
     * @param username the username to search for
     * @return UserTypeDetected enum value
     */
    private UserTypeDetected detectUserType(String username) {
        // Check User repository first
        if (userRepository.findByUsername(username).isPresent()) {
            return UserTypeDetected.USER;
        }
        
        // Check Driver repository (Driver extends Employee)
        if (driverRepository.findByUsername(username).isPresent()) {
            return UserTypeDetected.DRIVER;
        }
        
        // Check FleetManager repository (FleetManager extends Employee)
        if (fleetManagerRepository.findByUsername(username).isPresent()) {
            return UserTypeDetected.FLEET_MANAGER;
        }
        
        // Check Employee repository (generic Employee)
        if (employeeRepository.findByUsername(username).isPresent()) {
            return UserTypeDetected.EMPLOYEE;
        }
        
        // Not found in any repository
        return UserTypeDetected.NOT_FOUND;
    }
    
    /**
     * Helper method to detect object type using switch statement
     * 
     * @param obj the object to detect type for
     * @return UserTypeDetected enum value
     */
    private UserTypeDetected detectObjectType(Object obj) {
        if (obj == null) {
            return UserTypeDetected.NOT_FOUND;
        }
        
        // Use instanceof with switch (Java 16+)
        if (obj instanceof Driver) {
            return UserTypeDetected.DRIVER;
        } else if (obj instanceof FleetManager) {
            return UserTypeDetected.FLEET_MANAGER;
        } else if (obj instanceof Employee) {
            return UserTypeDetected.EMPLOYEE;
        } else if (obj instanceof User) {
            return UserTypeDetected.USER;
        }
        
        return UserTypeDetected.NOT_FOUND;
    }
}

