package com.htc.fleetmanagement.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.htc.fleetmanagement.repository.TokenRepository;

@Service
public class TokenCleanupService {
    
    private static final Logger logger = LoggerFactory.getLogger(TokenCleanupService.class);
    
    @Autowired
    private TokenRepository tokenRepository;
    

    
    //checks if token is expired or not....if true marked as expired  = true
    @Scheduled(fixedDelay = 300000, initialDelay = 60000)  // 5 minutes, wait 1 minute on startup
    public void markExpiredTokens() {
        try {
            LocalDateTime now = LocalDateTime.now();
            int count = 0;
            
            // Mark all tokens with expiresAt < now as expired
            var expiredTokens = tokenRepository.findExpiredTokens(now);
            if (!expiredTokens.isEmpty()) {
                for (var token : expiredTokens) {
                    token.setExpired(true);
                    tokenRepository.save(token);
                    count++;
                }
                logger.info("Marked {} expired tokens in database", count);
            }
        } catch (Exception e) {
            logger.error("Error marking expired tokens", e);
        }
    }
    
   //deletes token which are both revoked and expired
    @Scheduled(cron = "0 0 2 * * *")  
    public void cleanupRevokedTokens() {
        try {
            LocalDateTime cutoffDate = LocalDateTime.now().minusDays(7);
            tokenRepository.deleteRevokedTokensOlderThan(cutoffDate);
            logger.info("Cleaned up revoked tokens older than {}", cutoffDate);
        } catch (Exception e) {
            logger.error("Error cleaning up revoked tokens", e);
        }
    }
    
    //admin endpoint - manual cleanup of dead tokens
    public void triggerManualCleanup() {
        logger.info("Manual token cleanup triggered");
        markExpiredTokens();
        cleanupRevokedTokens();
        logger.info("Manual token cleanup completed");
    }
}
