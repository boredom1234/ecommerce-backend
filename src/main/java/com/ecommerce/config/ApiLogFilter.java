package com.ecommerce.config;

import com.ecommerce.service.ApiLogService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class ApiLogFilter extends OncePerRequestFilter {

    @Autowired
    private ApiLogService apiLogService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Wrap request and response to cache content for logging
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        
        long startTime = System.currentTimeMillis();
        
        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            // After request is processed
            long executionTime = System.currentTimeMillis() - startTime;
            
            String requestBody = "";
            // Only capture request body for specific content types
            if (request.getContentType() != null && 
                (request.getContentType().contains("application/json") || 
                 request.getContentType().contains("application/xml") ||
                 request.getContentType().contains("text/plain") ||
                 request.getContentType().contains("application/x-www-form-urlencoded"))) {
                
                byte[] content = requestWrapper.getContentAsByteArray();
                if (content.length > 0) {
                    requestBody = new String(content, StandardCharsets.UTF_8);
                    // Truncate if too long
                    if (requestBody.length() > 4000) {
                        requestBody = requestBody.substring(0, 4000) + "... (truncated)";
                    }
                }
            }
            
            // Log the API call
            apiLogService.createLog(requestWrapper, responseWrapper, requestBody).setExecutionTime(executionTime);
            
            // Don't forget to copy content to the original response
            responseWrapper.copyBodyToResponse();
        }
    }
} 