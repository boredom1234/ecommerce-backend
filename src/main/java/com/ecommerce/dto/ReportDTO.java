package com.ecommerce.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ReportDTO {
    
    private String reportName;
    private String reportType;
    private LocalDateTime generatedAt;
    private Map<String, Object> parameters;
    private Map<String, Object> summary;
    private List<Map<String, Object>> data;
    private String format;

    // Empty constructor
    public ReportDTO() {
    }

    // Constructor with fields
    public ReportDTO(String reportName, String reportType, Map<String, Object> parameters, 
                    Map<String, Object> summary, List<Map<String, Object>> data, String format) {
        this.reportName = reportName;
        this.reportType = reportType;
        this.generatedAt = LocalDateTime.now();
        this.parameters = parameters;
        this.summary = summary;
        this.data = data;
        this.format = format;
    }

    // Getters and Setters
    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public Map<String, Object> getSummary() {
        return summary;
    }

    public void setSummary(Map<String, Object> summary) {
        this.summary = summary;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
} 