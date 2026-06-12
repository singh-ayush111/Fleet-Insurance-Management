package com.htc.fleetmanagement.ratelimit;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

// Custom annotation for rate limiting
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RateLimiter {
    int limit() default 2;       
    int timeWindow() default 60; 
}
