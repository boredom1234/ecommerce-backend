package com.ecommerce.service;

import com.ecommerce.dto.ReportDTO;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.entity.OrderItem;
import com.ecommerce.entity.Role;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Generate a sales report based on specified parameters
     */
    public ReportDTO generateSalesReport(LocalDateTime startDate, LocalDateTime endDate, String format) {
        // Since we don't have a direct query method for date range, we'll fetch all orders and filter
        List<Order> allOrders = orderRepository.findAll();
        List<Order> orders = allOrders.stream()
                .filter(order -> {
                    LocalDateTime orderDate = order.getCreatedAt();
                    return !orderDate.isBefore(startDate) && !orderDate.isAfter(endDate);
                })
                .collect(Collectors.toList());
        
        // Calculate summary data
        BigDecimal totalRevenue = orders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        long totalOrders = orders.size();
        
        BigDecimal averageOrderValue = totalOrders > 0 
                ? totalRevenue.divide(new BigDecimal(totalOrders), 2, RoundingMode.HALF_UP) 
                : BigDecimal.ZERO;
        
        Map<Order.OrderStatus, Long> ordersByStatus = orders.stream()
                .collect(Collectors.groupingBy(Order::getStatus, Collectors.counting()));
        
        // Create summary map
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalRevenue", totalRevenue);
        summary.put("totalOrders", totalOrders);
        summary.put("averageOrderValue", averageOrderValue);
        summary.put("ordersByStatus", ordersByStatus);
        
        // Create detailed data
        List<Map<String, Object>> data = orders.stream().map(order -> {
            Map<String, Object> orderData = new HashMap<>();
            orderData.put("orderId", order.getId());
            orderData.put("orderDate", order.getCreatedAt()); // Using createdAt as the order date
            orderData.put("customer", order.getUser().getName());
            orderData.put("amount", order.getTotalAmount());
            orderData.put("status", order.getStatus());
            return orderData;
        }).collect(Collectors.toList());
        
        // Create parameters map
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("startDate", startDate);
        parameters.put("endDate", endDate);
        
        return new ReportDTO(
                "Sales Report", 
                "SALES", 
                parameters, 
                summary, 
                data, 
                format
        );
    }
    
    /**
     * Generate a product performance report
     */
    public ReportDTO generateProductReport(LocalDateTime startDate, LocalDateTime endDate, String format) {
        // Since we don't have a direct query method for date range, we'll fetch all orders and filter
        List<Order> allOrders = orderRepository.findAll();
        List<Order> orders = allOrders.stream()
                .filter(order -> {
                    LocalDateTime orderDate = order.getCreatedAt();
                    return !orderDate.isBefore(startDate) && !orderDate.isAfter(endDate);
                })
                .collect(Collectors.toList());
        
        // Create a map to track product sales and revenue
        Map<Long, Integer> productQuantitySold = new HashMap<>();
        Map<Long, BigDecimal> productRevenue = new HashMap<>();
        
        // Populate product sales and revenue data
        for (Order order : orders) {
            for (OrderItem item : order.getItems()) { // Using items instead of orderItems
                Long productId = item.getProduct().getId();
                
                // Update quantities
                productQuantitySold.put(
                        productId, 
                        productQuantitySold.getOrDefault(productId, 0) + item.getQuantity()
                );
                
                // Update revenue
                BigDecimal itemTotal = item.getPrice().multiply(new BigDecimal(item.getQuantity()));
                productRevenue.put(
                        productId, 
                        productRevenue.getOrDefault(productId, BigDecimal.ZERO).add(itemTotal)
                );
            }
        }
        
        // Get list of all products
        List<Product> allProducts = productRepository.findAll();
        
        // Create detailed data
        List<Map<String, Object>> data = allProducts.stream().map(product -> {
            Map<String, Object> productData = new HashMap<>();
            productData.put("productId", product.getId());
            productData.put("productName", product.getName());
            productData.put("category", product.getCategory());
            productData.put("price", product.getPrice());
            productData.put("currentStock", product.getStock());
            productData.put("quantitySold", productQuantitySold.getOrDefault(product.getId(), 0));
            productData.put("revenue", productRevenue.getOrDefault(product.getId(), BigDecimal.ZERO));
            return productData;
        }).collect(Collectors.toList());
        
        // Sort by revenue in descending order
        data.sort((a, b) -> {
            BigDecimal revenueA = (BigDecimal) a.get("revenue");
            BigDecimal revenueB = (BigDecimal) b.get("revenue");
            return revenueB.compareTo(revenueA);
        });
        
        // Calculate summary data
        int totalProductsSold = productQuantitySold.values().stream().mapToInt(Integer::intValue).sum();
        BigDecimal totalRevenue = productRevenue.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        int productsWithNoSales = (int) data.stream().filter(p -> (int) p.get("quantitySold") == 0).count();
        
        // Top 5 products
        List<Map<String, Object>> topProducts = data.stream().limit(5).collect(Collectors.toList());
        
        // Create summary map
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalProductsSold", totalProductsSold);
        summary.put("totalRevenue", totalRevenue);
        summary.put("productsWithNoSales", productsWithNoSales);
        summary.put("topProducts", topProducts);
        
        // Create parameters map
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("startDate", startDate);
        parameters.put("endDate", endDate);
        
        return new ReportDTO(
                "Product Performance Report", 
                "PRODUCT", 
                parameters, 
                summary, 
                data, 
                format
        );
    }
    
    /**
     * Generate a customer activity report
     */
    public ReportDTO generateCustomerReport(LocalDateTime startDate, LocalDateTime endDate, String format) {
        // Since we don't have a direct query method for date range, we'll fetch all orders and filter
        List<Order> allOrders = orderRepository.findAll();
        List<Order> orders = allOrders.stream()
                .filter(order -> {
                    LocalDateTime orderDate = order.getCreatedAt();
                    return !orderDate.isBefore(startDate) && !orderDate.isAfter(endDate);
                })
                .collect(Collectors.toList());
                
        List<User> customers = userRepository.findByRole(Role.USER);
        
        // Create maps to track customer activity
        Map<Long, List<Order>> customerOrders = new HashMap<>();
        
        // Group orders by customer
        for (Order order : orders) {
            Long userId = order.getUser().getId();
            if (!customerOrders.containsKey(userId)) {
                customerOrders.put(userId, new ArrayList<>());
            }
            customerOrders.get(userId).add(order);
        }
        
        // Create detailed data
        List<Map<String, Object>> data = customers.stream().map(customer -> {
            Map<String, Object> customerData = new HashMap<>();
            List<Order> customerOrdersList = customerOrders.getOrDefault(customer.getId(), Collections.emptyList());
            
            // Calculate customer metrics
            BigDecimal totalSpent = customerOrdersList.stream()
                    .map(Order::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            int orderCount = customerOrdersList.size();
            
            BigDecimal averageOrderValue = orderCount > 0 
                    ? totalSpent.divide(new BigDecimal(orderCount), 2, RoundingMode.HALF_UP) 
                    : BigDecimal.ZERO;
            
            // Populate customer data
            customerData.put("customerId", customer.getId());
            customerData.put("customerName", customer.getName());
            customerData.put("email", customer.getEmail());
            customerData.put("joinDate", customer.getCreatedAt());
            customerData.put("orderCount", orderCount);
            customerData.put("totalSpent", totalSpent);
            customerData.put("averageOrderValue", averageOrderValue);
            
            return customerData;
        }).collect(Collectors.toList());
        
        // Sort by total spent in descending order
        data.sort((a, b) -> {
            BigDecimal spentA = (BigDecimal) a.get("totalSpent");
            BigDecimal spentB = (BigDecimal) b.get("totalSpent");
            return spentB.compareTo(spentA);
        });
        
        // Calculate summary data
        long activeCustomers = data.stream().filter(c -> (int) c.get("orderCount") > 0).count();
        long inactiveCustomers = data.size() - activeCustomers;
        BigDecimal totalRevenue = data.stream()
                .map(c -> (BigDecimal) c.get("totalSpent"))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Top 5 customers
        List<Map<String, Object>> topCustomers = data.stream().limit(5).collect(Collectors.toList());
        
        // Create summary map
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalCustomers", data.size());
        summary.put("activeCustomers", activeCustomers);
        summary.put("inactiveCustomers", inactiveCustomers);
        summary.put("totalRevenue", totalRevenue);
        summary.put("topCustomers", topCustomers);
        
        // Create parameters map
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("startDate", startDate);
        parameters.put("endDate", endDate);
        
        return new ReportDTO(
                "Customer Activity Report", 
                "CUSTOMER", 
                parameters, 
                summary, 
                data, 
                format
        );
    }
    
    /**
     * Generate an inventory status report
     */
    public ReportDTO generateInventoryReport(String format) {
        List<Product> products = productRepository.findAll();
        
        // Create detailed data
        List<Map<String, Object>> data = products.stream().map(product -> {
            Map<String, Object> productData = new HashMap<>();
            productData.put("productId", product.getId());
            productData.put("productName", product.getName());
            productData.put("category", product.getCategory());
            productData.put("price", product.getPrice());
            productData.put("currentStock", product.getStock());
            
            String stockStatus;
            if (product.getStock() == 0) {
                stockStatus = "OUT_OF_STOCK";
            } else if (product.getStock() < 10) {
                stockStatus = "LOW_STOCK";
            } else {
                stockStatus = "ADEQUATE";
            }
            
            productData.put("stockStatus", stockStatus);
            return productData;
        }).collect(Collectors.toList());
        
        // Calculate summary data
        long totalProducts = products.size();
        long outOfStockProducts = data.stream()
                .filter(p -> p.get("stockStatus").equals("OUT_OF_STOCK"))
                .count();
        long lowStockProducts = data.stream()
                .filter(p -> p.get("stockStatus").equals("LOW_STOCK"))
                .count();
        
        BigDecimal inventoryValue = products.stream()
                .map(p -> p.getPrice().multiply(new BigDecimal(p.getStock())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Create summary map
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalProducts", totalProducts);
        summary.put("outOfStockProducts", outOfStockProducts);
        summary.put("lowStockProducts", lowStockProducts);
        summary.put("inventoryValue", inventoryValue);
        
        return new ReportDTO(
                "Inventory Status Report", 
                "INVENTORY", 
                new HashMap<>(), // No parameters needed for inventory report
                summary, 
                data, 
                format
        );
    }
} 