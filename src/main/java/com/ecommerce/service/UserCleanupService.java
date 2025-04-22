package com.ecommerce.service;

import com.ecommerce.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service responsible for cleaning up unverified user accounts.
 * It periodically checks for users who haven't verified their accounts
 * within the specified timeframe and deletes them.
 */
@Service
public class UserCleanupService {

    private static final Logger logger = LoggerFactory.getLogger(UserCleanupService.class);
    
    @Value("${user.verification.threshold.seconds:20}")
    private int verificationThresholdSeconds;

    @Autowired
    private UserRepository userRepository;

    /**
     * Scheduled task that runs periodically to check for and delete
     * unverified users who registered more than the configured threshold time ago.
     * 
     * The rate is configured in application.properties as user.verification.cleanup.interval.seconds
     */
    @Scheduled(fixedRateString = "${user.verification.cleanup.interval.seconds:10}000")
    @Transactional
    public void deleteUnverifiedUsers() {
        logger.debug("Running scheduled task to delete unverified users");
        
        // Calculate the threshold time (current time minus threshold seconds)
        LocalDateTime thresholdTime = LocalDateTime.now().minusSeconds(verificationThresholdSeconds);
        
        // Find all unverified users created before the threshold time
        List<Long> unverifiedUserIds = userRepository.findUnverifiedUserIdsCreatedBefore(thresholdTime);
        
        if (!unverifiedUserIds.isEmpty()) {
            logger.info("Deleting {} unverified users that have exceeded the verification timeframe of {} seconds", 
                    unverifiedUserIds.size(), verificationThresholdSeconds);
            
            // Delete all found users
            userRepository.deleteAllById(unverifiedUserIds);
            
            logger.info("Successfully deleted {} unverified users", unverifiedUserIds.size());
        } else {
            logger.debug("No unverified users found for deletion");
        }
    }
} 