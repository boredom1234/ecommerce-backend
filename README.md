## Automatic User Account Cleanup

The system automatically deletes unverified user accounts after a configurable period of time. This helps keep the database clean by removing incomplete registrations.

### Configuration

This behavior can be configured in `application.properties` with the following properties:

```properties
# Time in seconds after which unverified users will be deleted
user.verification.threshold.seconds=20

# How often the cleanup task runs (in seconds)
user.verification.cleanup.interval.seconds=10
```

### How it works

1. When a user registers, their account is initially marked as unverified (`is_verified=false`)
2. The user has a limited time (default: 20 seconds) to verify their account
3. A scheduled task runs periodically (default: every 10 seconds) to check for and delete any unverified users that have exceeded the verification timeframe

To disable this feature, you can set a very high value for `user.verification.threshold.seconds` in the application properties. 