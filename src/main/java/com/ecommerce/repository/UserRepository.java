package com.ecommerce.repository;

import com.ecommerce.entity.User;
import com.ecommerce.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByResetToken(String resetToken);
    
    // Methods for searching users
    List<User> findByEmailContainingIgnoreCase(String email);
    List<User> findByNameContainingIgnoreCase(String name);
    
    // Methods for filtering users by role
    List<User> findByRole(Role role);
    Page<User> findByRole(Role role, Pageable pageable);
    
    // Methods for counting users
    long countByRole(Role role);
    long countByRoleNot(Role role);
    long countByAccountLocked(boolean locked);
    long countByVerified(boolean verified);
    
    /**
     * Find IDs of unverified users created before the specified time.
     * Uses 'verified = false' to find users who haven't verified their accounts yet.
     *
     * @param createdBefore The threshold timestamp
     * @return List of user IDs that match the criteria
     */
    @Query("SELECT u.id FROM User u WHERE u.verified = false AND u.createdAt < :createdBefore")
    List<Long> findUnverifiedUserIdsCreatedBefore(@Param("createdBefore") LocalDateTime createdBefore);
    
    /**
     * Count users who have placed at least one order
     * 
     * @return Count of users with orders
     */
    @Query("SELECT COUNT(DISTINCT o.user.id) FROM Order o")
    long countUsersWithOrders();
    
    /**
     * Find top customers by total order value
     * Returns the top 10 customers by default
     * 
     * @return List of maps containing user data and total spent
     */
    @Query(value = "SELECT u.id as id, u.name as name, u.email as email, " +
                  "SUM(o.total_amount) as totalSpent, COUNT(o.id) as orderCount " +
                  "FROM users u " +
                  "JOIN orders o ON u.id = o.user_id " +
                  "WHERE o.status != 'CANCELLED' " +
                  "GROUP BY u.id, u.name, u.email " +
                  "ORDER BY totalSpent DESC " +
                  "LIMIT 10", nativeQuery = true)
    List<Map<String, Object>> findTopCustomersByOrderValue();
} 