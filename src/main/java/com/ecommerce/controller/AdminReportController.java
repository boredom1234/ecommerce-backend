package com.ecommerce.controller;

import com.ecommerce.dto.ApiResponse;
import com.ecommerce.dto.ReportDTO;
import com.ecommerce.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin/reports")
public class AdminReportController {

    @Autowired
    private ReportService reportService;

    /**
     * Generate a sales report for the specified time period
     */
    @GetMapping("/sales")
    public ResponseEntity<?> generateSalesReport(
            @RequestParam(name = "start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(name = "end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(name = "format", defaultValue = "JSON") String format) {
        
        ReportDTO report = reportService.generateSalesReport(startDate, endDate, format);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Sales report generated successfully", report));
    }

    /**
     * Generate a product performance report for the specified time period
     */
    @GetMapping("/products")
    public ResponseEntity<?> generateProductReport(
            @RequestParam(name = "start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(name = "end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(name = "format", defaultValue = "JSON") String format) {
        
        ReportDTO report = reportService.generateProductReport(startDate, endDate, format);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Product performance report generated successfully", report));
    }

    /**
     * Generate a customer activity report for the specified time period
     */
    @GetMapping("/customers")
    public ResponseEntity<?> generateCustomerReport(
            @RequestParam(name = "start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(name = "end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(name = "format", defaultValue = "JSON") String format) {
        
        ReportDTO report = reportService.generateCustomerReport(startDate, endDate, format);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Customer activity report generated successfully", report));
    }

    /**
     * Generate an inventory status report
     */
    @GetMapping("/inventory")
    public ResponseEntity<?> generateInventoryReport(
            @RequestParam(name = "format", defaultValue = "JSON") String format) {
        
        ReportDTO report = reportService.generateInventoryReport(format);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Inventory status report generated successfully", report));
    }
    
    /**
     * Generate a comprehensive dashboard report that includes key metrics from all areas
     */
    @GetMapping("/dashboard")
    public ResponseEntity<?> generateDashboardReport(
            @RequestParam(name = "format", defaultValue = "JSON") String format) {
        
        // Start date for time-based metrics (30 days ago)
        LocalDateTime startDate = LocalDateTime.now().minusDays(30);
        LocalDateTime endDate = LocalDateTime.now();
        
        // Create reports for each area
        ReportDTO salesReport = reportService.generateSalesReport(startDate, endDate, format);
        ReportDTO productReport = reportService.generateProductReport(startDate, endDate, format);
        ReportDTO customerReport = reportService.generateCustomerReport(startDate, endDate, format);
        ReportDTO inventoryReport = reportService.generateInventoryReport(format);
        
        // Combine the summaries from each report
        ReportDTO dashboardReport = new ReportDTO();
        dashboardReport.setReportName("Dashboard Report");
        dashboardReport.setReportType("DASHBOARD");
        dashboardReport.setGeneratedAt(LocalDateTime.now());
        dashboardReport.setFormat(format);
        
        // Add time period as parameter
        dashboardReport.setParameters(salesReport.getParameters());
        
        // Combine summaries
        dashboardReport.setSummary(salesReport.getSummary());
        dashboardReport.getSummary().putAll(productReport.getSummary());
        dashboardReport.getSummary().putAll(customerReport.getSummary());
        dashboardReport.getSummary().putAll(inventoryReport.getSummary());
        
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Dashboard report generated successfully", dashboardReport));
    }
} 