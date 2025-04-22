package com.ecommerce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Configuration class to enable scheduling functionality in the application.
 * This allows us to use @Scheduled annotations for tasks that need to run
 * at specific intervals.
 */
@Configuration
@EnableScheduling
public class SchedulerConfig {
    // No additional configuration needed, the @EnableScheduling annotation
    // activates Spring's scheduling capabilities
} 