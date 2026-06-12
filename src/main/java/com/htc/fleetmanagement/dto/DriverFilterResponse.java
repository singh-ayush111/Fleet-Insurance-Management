package com.htc.fleetmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response wrapper for Driver Filter API endpoints
 * 
 * Used to provide consistent response format across all filtering endpoints
 * with success status, message, and data payload.
 * 
 * @param <T> Generic type for data payload
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DriverFilterResponse<T> {
    
    private boolean success;
    
    private String message;
    
    private T data;
    
    @Override
    public String toString() {
        return "DriverFilterResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
