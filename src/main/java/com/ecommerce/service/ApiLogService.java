package com.ecommerce.service;

import com.ecommerce.dto.ApiLogDTO;
import com.ecommerce.entity.ApiLog;
import com.ecommerce.entity.Role;
import com.ecommerce.entity.User;
import com.ecommerce.repository.ApiLogRepository;
import com.ecommerce.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApiLogService {

    @Autowired
    private ApiLogRepository apiLogRepository;

    @Autowired
    private UserRepository userRepository;

    public ApiLog createLog(HttpServletRequest request, HttpServletResponse response, String requestBody) {
        ApiLog log = new ApiLog();
        log.setEndpoint(request.getRequestURI());
        log.setMethod(request.getMethod());
        log.setIpAddress(getClientIp(request));
        log.setUserAgent(request.getHeader("User-Agent"));
        log.setRequestBody(requestBody);
        log.setResponseStatus(response.getStatus());
        log.setTimestamp(LocalDateTime.now());

        // Get current user if authenticated
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            String email = auth.getName();
            Optional<User> userOpt = userRepository.findByEmail(email);
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                log.setUserId(user.getId());
                log.setUserEmail(user.getEmail());
                log.setUserRole(user.getRole());
            }
        }

        return apiLogRepository.save(log);
    }

    public Page<ApiLogDTO> getAllLogs(Pageable pageable) {
        return apiLogRepository.findAllByOrderByTimestampDesc(pageable).map(this::convertToDto);
    }

    public Page<ApiLogDTO> getLogsByUser(Long userId, Pageable pageable) {
        return apiLogRepository.findByUserIdOrderByTimestampDesc(userId, pageable).map(this::convertToDto);
    }

    public Page<ApiLogDTO> getLogsByEmail(String email, Pageable pageable) {
        return apiLogRepository.findByUserEmailContainingIgnoreCaseOrderByTimestampDesc(email, pageable).map(this::convertToDto);
    }

    public Page<ApiLogDTO> getLogsByEndpoint(String endpoint, Pageable pageable) {
        return apiLogRepository.findByEndpointContainingOrderByTimestampDesc(endpoint, pageable).map(this::convertToDto);
    }

    public Page<ApiLogDTO> getLogsByMethod(String method, Pageable pageable) {
        return apiLogRepository.findByMethodOrderByTimestampDesc(method, pageable).map(this::convertToDto);
    }

    public Page<ApiLogDTO> getLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return apiLogRepository.findByTimestampBetweenOrderByTimestampDesc(startDate, endDate, pageable).map(this::convertToDto);
    }

    public Page<ApiLogDTO> getLogsByStatus(Integer status, Pageable pageable) {
        return apiLogRepository.findByResponseStatusOrderByTimestampDesc(status, pageable).map(this::convertToDto);
    }

    private ApiLogDTO convertToDto(ApiLog apiLog) {
        ApiLogDTO dto = new ApiLogDTO();
        dto.setId(apiLog.getId());
        dto.setEndpoint(apiLog.getEndpoint());
        dto.setMethod(apiLog.getMethod());
        dto.setUserId(apiLog.getUserId());
        dto.setUserEmail(apiLog.getUserEmail());
        dto.setUserRole(apiLog.getUserRole());
        dto.setRequestBody(apiLog.getRequestBody());
        dto.setResponseStatus(apiLog.getResponseStatus());
        dto.setIpAddress(apiLog.getIpAddress());
        dto.setUserAgent(apiLog.getUserAgent());
        dto.setTimestamp(apiLog.getTimestamp());
        dto.setExecutionTime(apiLog.getExecutionTime());
        return dto;
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0];
        }
        return request.getRemoteAddr();
    }
} 