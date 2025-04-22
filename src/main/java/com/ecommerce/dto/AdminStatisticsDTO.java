package com.ecommerce.dto;

import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class AdminStatisticsDTO {
    // Financial statistics
    private BigDecimal totalRevenue;
    private BigDecimal monthlyRevenue;
    private BigDecimal weeklyRevenue;
    private BigDecimal dailyRevenue;
    private BigDecimal averageOrderValue;
    
    // Order statistics
    private long totalOrders;
    private long pendingOrders;
    private long confirmedOrders;
    private long shippedOrders;
    private long deliveredOrders;
    private long cancelledOrders;
    private double conversionRate;
    
    // Product statistics
    private long totalProducts;
    private long lowStockProducts;
    private long outOfStockProducts;
    private List<Map<String, Object>> topSellingProducts;
    
    // Customer statistics
    private long totalCustomers;
    private long newCustomersThisMonth;
    private long activeCustomers;
    private List<Map<String, Object>> topCustomers;
    
    // User statistics
    private long totalUsersCount;
    private Map<String, Long> usersByRole = new HashMap<>();
    
    // Time-based statistics
    private Map<String, BigDecimal> revenueByMonth = new HashMap<>();
    private Map<String, BigDecimal> revenueByDay = new HashMap<>();
    private Map<String, Long> ordersByMonth = new HashMap<>();
    private Map<String, Long> ordersByDay = new HashMap<>();
    
    // Getters and setters
    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getMonthlyRevenue() {
        return monthlyRevenue;
    }

    public void setMonthlyRevenue(BigDecimal monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }

    public BigDecimal getWeeklyRevenue() {
        return weeklyRevenue;
    }

    public void setWeeklyRevenue(BigDecimal weeklyRevenue) {
        this.weeklyRevenue = weeklyRevenue;
    }

    public BigDecimal getDailyRevenue() {
        return dailyRevenue;
    }

    public void setDailyRevenue(BigDecimal dailyRevenue) {
        this.dailyRevenue = dailyRevenue;
    }

    public BigDecimal getAverageOrderValue() {
        return averageOrderValue;
    }

    public void setAverageOrderValue(BigDecimal averageOrderValue) {
        this.averageOrderValue = averageOrderValue;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public long getPendingOrders() {
        return pendingOrders;
    }

    public void setPendingOrders(long pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

    public long getConfirmedOrders() {
        return confirmedOrders;
    }

    public void setConfirmedOrders(long confirmedOrders) {
        this.confirmedOrders = confirmedOrders;
    }

    public long getShippedOrders() {
        return shippedOrders;
    }

    public void setShippedOrders(long shippedOrders) {
        this.shippedOrders = shippedOrders;
    }

    public long getDeliveredOrders() {
        return deliveredOrders;
    }

    public void setDeliveredOrders(long deliveredOrders) {
        this.deliveredOrders = deliveredOrders;
    }

    public long getCancelledOrders() {
        return cancelledOrders;
    }

    public void setCancelledOrders(long cancelledOrders) {
        this.cancelledOrders = cancelledOrders;
    }

    public double getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(double conversionRate) {
        this.conversionRate = conversionRate;
    }

    public long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public long getLowStockProducts() {
        return lowStockProducts;
    }

    public void setLowStockProducts(long lowStockProducts) {
        this.lowStockProducts = lowStockProducts;
    }

    public long getOutOfStockProducts() {
        return outOfStockProducts;
    }

    public void setOutOfStockProducts(long outOfStockProducts) {
        this.outOfStockProducts = outOfStockProducts;
    }

    public List<Map<String, Object>> getTopSellingProducts() {
        return topSellingProducts;
    }

    public void setTopSellingProducts(List<Map<String, Object>> topSellingProducts) {
        this.topSellingProducts = topSellingProducts;
    }

    public long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public long getNewCustomersThisMonth() {
        return newCustomersThisMonth;
    }

    public void setNewCustomersThisMonth(long newCustomersThisMonth) {
        this.newCustomersThisMonth = newCustomersThisMonth;
    }

    public long getActiveCustomers() {
        return activeCustomers;
    }

    public void setActiveCustomers(long activeCustomers) {
        this.activeCustomers = activeCustomers;
    }

    public List<Map<String, Object>> getTopCustomers() {
        return topCustomers;
    }

    public void setTopCustomers(List<Map<String, Object>> topCustomers) {
        this.topCustomers = topCustomers;
    }

    public long getTotalUsersCount() {
        return totalUsersCount;
    }

    public void setTotalUsersCount(long totalUsersCount) {
        this.totalUsersCount = totalUsersCount;
    }

    public Map<String, Long> getUsersByRole() {
        return usersByRole;
    }

    public void setUsersByRole(Map<String, Long> usersByRole) {
        this.usersByRole = usersByRole;
    }

    public Map<String, BigDecimal> getRevenueByMonth() {
        return revenueByMonth;
    }

    public void setRevenueByMonth(Map<String, BigDecimal> revenueByMonth) {
        this.revenueByMonth = revenueByMonth;
    }

    public Map<String, BigDecimal> getRevenueByDay() {
        return revenueByDay;
    }

    public void setRevenueByDay(Map<String, BigDecimal> revenueByDay) {
        this.revenueByDay = revenueByDay;
    }

    public Map<String, Long> getOrdersByMonth() {
        return ordersByMonth;
    }

    public void setOrdersByMonth(Map<String, Long> ordersByMonth) {
        this.ordersByMonth = ordersByMonth;
    }

    public Map<String, Long> getOrdersByDay() {
        return ordersByDay;
    }

    public void setOrdersByDay(Map<String, Long> ordersByDay) {
        this.ordersByDay = ordersByDay;
    }
} 