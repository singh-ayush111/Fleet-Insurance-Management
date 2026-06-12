package com.htc.fleetmanagement.ratelimit;

import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Aspect
@Component
public class RateLimitingAspect {

    private final Map<String, List<Long>> requestLog = new ConcurrentHashMap<>();

    @Around("@annotation(rateLimited)")
    public Object enforceRateLimit(ProceedingJoinPoint pjp, RateLimiter rateLimited) throws Throwable {
    	// Use method signature as the key for rate limiting
        String key = pjp.getSignature().toShortString();
        long now = System.currentTimeMillis();

        // Initialize the request log for this key if it doesn't exist
        requestLog.putIfAbsent(key, new ArrayList<>());
        List<Long> timestamps = requestLog.get(key);

        // Remove timestamps that are outside the time window
        timestamps.removeIf(t -> (now - t) > rateLimited.timeWindow() * 1000);

        // Check if the number of requests in the current time window exceeds the limit
        if (timestamps.size() >= rateLimited.limit()) {
            throw new RuntimeException("Rate limit exceeded for " + key);
        }

        // Add the current timestamp to the log
        timestamps.add(now);
        return pjp.proceed();
    }
}
