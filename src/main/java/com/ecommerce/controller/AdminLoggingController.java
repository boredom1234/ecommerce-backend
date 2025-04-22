package com.ecommerce.controller;

import com.ecommerce.dto.ApiLogDTO;
import com.ecommerce.dto.ApiResponse;
import com.ecommerce.service.ApiLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin/logs")
public class AdminLoggingController {

    @Autowired
    private ApiLogService apiLogService;

    /**
     * Get all API logs with pagination
     */
    @GetMapping
    public ResponseEntity<?> getAllLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ApiLogDTO> logs = apiLogService.getAllLogs(PageRequest.of(page, size));
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "API logs retrieved successfully", logs));
    }

    /**
     * Get logs by user ID
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getLogsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ApiLogDTO> logs = apiLogService.getLogsByUser(userId, PageRequest.of(page, size));
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "User API logs retrieved successfully", logs));
    }

    /**
     * Get logs by user email (partial match)
     */
    @GetMapping("/email")
    public ResponseEntity<?> getLogsByEmail(
            @RequestParam String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ApiLogDTO> logs = apiLogService.getLogsByEmail(email, PageRequest.of(page, size));
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Email API logs retrieved successfully", logs));
    }

    /**
     * Get logs by endpoint (partial match)
     */
    @GetMapping("/endpoint")
    public ResponseEntity<?> getLogsByEndpoint(
            @RequestParam String endpoint,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ApiLogDTO> logs = apiLogService.getLogsByEndpoint(endpoint, PageRequest.of(page, size));
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Endpoint API logs retrieved successfully", logs));
    }

    /**
     * Get logs by HTTP method
     */
    @GetMapping("/method")
    public ResponseEntity<?> getLogsByMethod(
            @RequestParam String method,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ApiLogDTO> logs = apiLogService.getLogsByMethod(method, PageRequest.of(page, size));
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Method API logs retrieved successfully", logs));
    }

    /**
     * Get logs by date range
     */
    @GetMapping("/date-range")
    public ResponseEntity<?> getLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ApiLogDTO> logs = apiLogService.getLogsByDateRange(startDate, endDate, PageRequest.of(page, size));
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Date range API logs retrieved successfully", logs));
    }

    /**
     * Get logs by HTTP status code
     */
    @GetMapping("/status")
    public ResponseEntity<?> getLogsByStatus(
            @RequestParam Integer status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ApiLogDTO> logs = apiLogService.getLogsByStatus(status, PageRequest.of(page, size));
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Status API logs retrieved successfully", logs));
    }
} 