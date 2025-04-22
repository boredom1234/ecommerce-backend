package com.ecommerce.repository;

import com.ecommerce.entity.ApiLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ApiLogRepository extends JpaRepository<ApiLog, Long> {
    Page<ApiLog> findByUserIdOrderByTimestampDesc(Long userId, Pageable pageable);
    Page<ApiLog> findByUserEmailContainingIgnoreCaseOrderByTimestampDesc(String email, Pageable pageable);
    Page<ApiLog> findByEndpointContainingOrderByTimestampDesc(String endpoint, Pageable pageable);
    Page<ApiLog> findByMethodOrderByTimestampDesc(String method, Pageable pageable);
    Page<ApiLog> findByTimestampBetweenOrderByTimestampDesc(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    Page<ApiLog> findByResponseStatusOrderByTimestampDesc(Integer status, Pageable pageable);
    Page<ApiLog> findAllByOrderByTimestampDesc(Pageable pageable);
} 