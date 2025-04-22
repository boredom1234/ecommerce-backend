package com.ecommerce.service;

import com.ecommerce.dto.AdminProductDTO;
import com.ecommerce.dto.OrderDTO;
import com.ecommerce.dto.UserDTO;
import com.ecommerce.dto.AdminProfileDTO;
import com.ecommerce.dto.UserProfileDTO;
import com.ecommerce.dto.AdminStatisticsDTO;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.entity.Role;
import com.ecommerce.entity.OrderItem;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

@Service
public class AdminService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Product Management
    public AdminProductDTO addProduct(AdminProductDTO productDTO) {
        Product product = new Product();
        updateProductFromDTO(product, productDTO);
        product = productRepository.save(product);
        return convertToAdminProductDTO(product);
    }

    public AdminProductDTO updateProduct(Long id, AdminProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        updateProductFromDTO(product, productDTO);
        product = productRepository.save(product);
        return convertToAdminProductDTO(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Page<AdminProductDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::convertToAdminProductDTO);
    }

    @Transactional
    public AdminProductDTO updateProductStock(Long id, Integer stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        product.setStock(stock);
        product = productRepository.save(product);
        
        return convertToAdminProductDTO(product);
    }
    
    public List<AdminProductDTO> getLowStockProducts(Integer threshold) {
        return productRepository.findByStockLessThanEqual(threshold)
                .stream()
                .map(this::convertToAdminProductDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public List<AdminProductDTO> bulkUpdateStock(List<ProductStockUpdateRequest> updates) {
        List<AdminProductDTO> updatedProducts = new ArrayList<>();
        
        for (ProductStockUpdateRequest update : updates) {
            if (update.getStock() < 0) {
                throw new IllegalArgumentException("Stock cannot be negative for product ID: " + update.getProductId());
            }
            
            Product product = productRepository.findById(update.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + update.getProductId()));
            
            product.setStock(update.getStock());
            product = productRepository.save(product);
            updatedProducts.add(convertToAdminProductDTO(product));
        }
        
        return updatedProducts;
    }
    
    public static class ProductStockUpdateRequest {
        private Long productId;
        private Integer stock;
        
        // Getters and setters
        public Long getProductId() {
            return productId;
        }
        
        public void setProductId(Long productId) {
            this.productId = productId;
        }
        
        public Integer getStock() {
            return stock;
        }
        
        public void setStock(Integer stock) {
            this.stock = stock;
        }
    }

    // Order Management
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToOrderDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return convertToOrderDTO(order);
    }

    @Transactional
    public OrderDTO updateOrderStatus(Long id, Order.OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // If the order is being cancelled and was not already cancelled or delivered,
        // we need to return the products to stock
        if (status == Order.OrderStatus.CANCELLED && 
            order.getStatus() != Order.OrderStatus.CANCELLED &&
            order.getStatus() != Order.OrderStatus.DELIVERED) {
            
            // Return product stock when cancelling an order
            for (OrderItem item : order.getItems()) {
                Product product = item.getProduct();
                Integer newStock = product.getStock() + item.getQuantity();
                product.setStock(newStock);
                productRepository.save(product);
            }
        }
        
        order.setStatus(status);
        order = orderRepository.save(order);
        return convertToOrderDTO(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    // User Management
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }
    
    public Page<UserDTO> getUsersWithPagination(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::convertToUserDTO);
    }
    
    public Page<UserDTO> getUsersByRole(Role role, Pageable pageable) {
        return userRepository.findByRole(role, pageable)
                .map(this::convertToUserDTO);
    }
    
    public List<UserDTO> searchUsersByEmail(String emailFragment) {
        return userRepository.findByEmailContainingIgnoreCase(emailFragment).stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }
    
    public List<UserDTO> searchUsersByName(String nameFragment) {
        return userRepository.findByNameContainingIgnoreCase(nameFragment).stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToUserDTO(user);
    }

    @Transactional
    public UserDTO updateUserRole(Long id, Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(role);
        user = userRepository.save(user);
        return convertToUserDTO(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    // Advanced User and Admin Profile Management
    
    public UserProfileDTO getUserProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        UserProfileDTO profileDTO = new UserProfileDTO(convertToUserDTO(user));
        profileDTO.setVerified(user.isVerified());
        profileDTO.setCreatedAt(user.getCreatedAt());
        profileDTO.setLastLogin(user.getLastLogin());
        profileDTO.setAccountLocked(user.isAccountLocked());
        
        // Get order statistics
        List<Order> userOrders = orderRepository.findByUserId(id);
        profileDTO.setOrderCount(userOrders.size());
        
        // Calculate total spent using BigDecimal
        BigDecimal totalSpent = userOrders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        profileDTO.setTotalSpent(totalSpent.doubleValue());
        
        // Get the latest order date
        Optional<LocalDateTime> latestOrderDate = userOrders.stream()
                .map(Order::getCreatedAt)
                .max(LocalDateTime::compareTo);
        latestOrderDate.ifPresent(profileDTO::setLastOrderDate);
        
        // Set account status
        if (user.isAccountLocked()) {
            profileDTO.setAccountStatus("LOCKED");
        } else if (!user.isVerified()) {
            profileDTO.setAccountStatus("UNVERIFIED");
        } else {
            profileDTO.setAccountStatus("ACTIVE");
        }
        
        return profileDTO;
    }
    
    public List<UserProfileDTO> getAllUserProfiles() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserDTO userDTO = convertToUserDTO(user);
                    UserProfileDTO profileDTO = new UserProfileDTO(userDTO);
                    profileDTO.setVerified(user.isVerified());
                    profileDTO.setCreatedAt(user.getCreatedAt());
                    profileDTO.setLastLogin(user.getLastLogin());
                    profileDTO.setAccountLocked(user.isAccountLocked());
                    
                    if (user.isAccountLocked()) {
                        profileDTO.setAccountStatus("LOCKED");
                    } else if (!user.isVerified()) {
                        profileDTO.setAccountStatus("UNVERIFIED");
                    } else {
                        profileDTO.setAccountStatus("ACTIVE");
                    }
                    
                    return profileDTO;
                })
                .collect(Collectors.toList());
    }
    
    public List<AdminProfileDTO> getAllAdminProfiles() {
        return userRepository.findByRole(Role.ADMIN).stream()
                .map(this::convertToAdminProfileDTO)
                .collect(Collectors.toList());
    }
    
    public AdminProfileDTO getAdminProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        if (user.getRole() != Role.ADMIN) {
            throw new RuntimeException("User is not an admin");
        }
        return convertToAdminProfileDTO(user);
    }
    
    @Transactional
    public UserProfileDTO updateUserProfile(Long id, UserProfileDTO profileDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Update user profile fields
        if (profileDTO.getName() != null) {
            user.setName(profileDTO.getName());
        }
        
        if (profileDTO.getAddress() != null) {
            user.setAddress(profileDTO.getAddress());
        }
        
        if (profileDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(profileDTO.getPhoneNumber());
        }
        
        if (profileDTO.getRole() != null) {
            user.setRole(profileDTO.getRole());
        }
        
        // Account status management
        if (profileDTO.getAccountStatus() != null) {
            switch (profileDTO.getAccountStatus()) {
                case "ACTIVE":
                    user.setVerified(true);
                    user.setAccountLocked(false);
                    break;
                case "LOCKED":
                    user.setAccountLocked(true);
                    break;
                case "UNVERIFIED":
                    user.setVerified(false);
                    user.setAccountLocked(false);
                    break;
                default:
                    // Ignore unknown status
                    break;
            }
        }
        
        User updatedUser = userRepository.save(user);
        return getUserProfile(updatedUser.getId());
    }
    
    @Transactional
    public AdminProfileDTO updateAdminProfile(Long id, AdminProfileDTO profileDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        
        if (user.getRole() != Role.ADMIN) {
            throw new RuntimeException("User is not an admin");
        }
        
        // Update admin profile fields
        if (profileDTO.getName() != null) {
            user.setName(profileDTO.getName());
        }
        
        if (profileDTO.getAddress() != null) {
            user.setAddress(profileDTO.getAddress());
        }
        
        if (profileDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(profileDTO.getPhoneNumber());
        }
        
        User updatedUser = userRepository.save(user);
        return convertToAdminProfileDTO(updatedUser);
    }
    
    @Transactional
    public void toggleUserAccountLock(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setAccountLocked(!user.isAccountLocked());
        userRepository.save(user);
    }
    
    @Transactional
    public void resetUserPassword(Long id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    
    public Map<String, Long> getUsersStatistics() {
        Map<String, Long> stats = new HashMap<>();
        
        // Get counts by role
        long adminCount = userRepository.countByRole(Role.ADMIN);
        long staffCount = userRepository.countByRole(Role.STAFF);
        long userCount = userRepository.countByRole(Role.USER);
        
        // Get counts by status
        long lockedCount = userRepository.countByAccountLocked(true);
        long unverifiedCount = userRepository.countByVerified(false);
        
        stats.put("totalUsers", adminCount + staffCount + userCount);
        stats.put("adminCount", adminCount);
        stats.put("staffCount", staffCount);
        stats.put("customerCount", userCount);
        stats.put("lockedAccounts", lockedCount);
        stats.put("unverifiedAccounts", unverifiedCount);
        
        return stats;
    }
    
    public AdminStatisticsDTO getAdminDashboardStatistics() {
        AdminStatisticsDTO stats = new AdminStatisticsDTO();
        
        // Get all orders for revenue calculations
        List<Order> allOrders = orderRepository.findAll();
        
        // Calculate financial statistics
        calculateFinancialStatistics(stats, allOrders);
        
        // Calculate order statistics
        calculateOrderStatistics(stats, allOrders);
        
        // Calculate product statistics
        calculateProductStatistics(stats);
        
        // Calculate customer statistics
        calculateCustomerStatistics(stats, allOrders);
        
        // Calculate time-based statistics
        calculateTimeBasedStatistics(stats, allOrders);
        
        // Calculate total users count and statistics by role
        calculateUserStatistics(stats);
        
        return stats;
    }
    
    private void calculateFinancialStatistics(AdminStatisticsDTO stats, List<Order> allOrders) {
        // Total revenue
        BigDecimal totalRevenue = allOrders.stream()
                .filter(order -> order.getStatus() != Order.OrderStatus.CANCELLED)
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.setTotalRevenue(totalRevenue);
        
        // Calculate average order value
        if (!allOrders.isEmpty()) {
            BigDecimal averageOrderValue = totalRevenue.divide(
                    BigDecimal.valueOf(allOrders.stream()
                            .filter(order -> order.getStatus() != Order.OrderStatus.CANCELLED)
                            .count()),
                    2, BigDecimal.ROUND_HALF_UP);
            stats.setAverageOrderValue(averageOrderValue);
        } else {
            stats.setAverageOrderValue(BigDecimal.ZERO);
        }
        
        // Calculate current day, week, and month revenue
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime startOfWeek = now.toLocalDate().minusDays(now.getDayOfWeek().getValue() - 1).atStartOfDay();
        LocalDateTime startOfMonth = now.toLocalDate().withDayOfMonth(1).atStartOfDay();
        
        BigDecimal dailyRevenue = allOrders.stream()
                .filter(order -> order.getStatus() != Order.OrderStatus.CANCELLED)
                .filter(order -> order.getCreatedAt().isAfter(startOfDay))
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.setDailyRevenue(dailyRevenue);
        
        BigDecimal weeklyRevenue = allOrders.stream()
                .filter(order -> order.getStatus() != Order.OrderStatus.CANCELLED)
                .filter(order -> order.getCreatedAt().isAfter(startOfWeek))
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.setWeeklyRevenue(weeklyRevenue);
        
        BigDecimal monthlyRevenue = allOrders.stream()
                .filter(order -> order.getStatus() != Order.OrderStatus.CANCELLED)
                .filter(order -> order.getCreatedAt().isAfter(startOfMonth))
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.setMonthlyRevenue(monthlyRevenue);
    }
    
    private void calculateOrderStatistics(AdminStatisticsDTO stats, List<Order> allOrders) {
        // Basic order counts
        stats.setTotalOrders(allOrders.size());
        
        stats.setPendingOrders(allOrders.stream()
                .filter(order -> order.getStatus() == Order.OrderStatus.PENDING)
                .count());
        
        stats.setConfirmedOrders(allOrders.stream()
                .filter(order -> order.getStatus() == Order.OrderStatus.CONFIRMED)
                .count());
        
        stats.setShippedOrders(allOrders.stream()
                .filter(order -> order.getStatus() == Order.OrderStatus.SHIPPED)
                .count());
        
        stats.setDeliveredOrders(allOrders.stream()
                .filter(order -> order.getStatus() == Order.OrderStatus.DELIVERED)
                .count());
        
        stats.setCancelledOrders(allOrders.stream()
                .filter(order -> order.getStatus() == Order.OrderStatus.CANCELLED)
                .count());
        
        // Calculate conversion rate (assuming each user visited the site once)
        long totalUsers = userRepository.countByRole(Role.USER);
        long usersWithOrders = userRepository.countUsersWithOrders();
        
        if (totalUsers > 0) {
            double conversionRate = (double) usersWithOrders / totalUsers * 100;
            stats.setConversionRate(conversionRate);
        }
    }
    
    private void calculateProductStatistics(AdminStatisticsDTO stats) {
        List<Product> allProducts = productRepository.findAll();
        
        // Basic product counts
        stats.setTotalProducts(allProducts.size());
        
        stats.setLowStockProducts(allProducts.stream()
                .filter(product -> product.getStock() > 0 && product.getStock() <= 10)
                .count());
        
        stats.setOutOfStockProducts(allProducts.stream()
                .filter(product -> product.getStock() == 0)
                .count());
        
        // Get top selling products
        List<Map<String, Object>> topProducts = productRepository.findTopSellingProducts();
        stats.setTopSellingProducts(topProducts);
    }
    
    private void calculateCustomerStatistics(AdminStatisticsDTO stats, List<Order> allOrders) {
        // Get all users with ROLE_USER
        List<User> customers = userRepository.findByRole(Role.USER);
        
        // Basic customer counts
        stats.setTotalCustomers(customers.size());
        
        // Get customers created in the current month
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        stats.setNewCustomersThisMonth(customers.stream()
                .filter(user -> user.getCreatedAt() != null && user.getCreatedAt().isAfter(startOfMonth))
                .count());
        
        // Active customers (placed at least one order)
        Set<Long> customerIdsWithOrders = allOrders.stream()
                .map(order -> order.getUser().getId())
                .collect(Collectors.toSet());
        stats.setActiveCustomers(customerIdsWithOrders.size());
        
        // Get top customers by order value
        List<Map<String, Object>> topCustomers = userRepository.findTopCustomersByOrderValue();
        stats.setTopCustomers(topCustomers);
    }
    
    private void calculateTimeBasedStatistics(AdminStatisticsDTO stats, List<Order> allOrders) {
        // Group orders by month and day for the past year
        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
        
        // Filter orders from the past year
        List<Order> recentOrders = allOrders.stream()
                .filter(order -> order.getCreatedAt().isAfter(oneYearAgo))
                .collect(Collectors.toList());
        
        // Group by month (format: "2023-01")
        Map<String, List<Order>> ordersByMonth = recentOrders.stream()
                .collect(Collectors.groupingBy(order -> 
                    order.getCreatedAt().getYear() + "-" + 
                    String.format("%02d", order.getCreatedAt().getMonthValue())));
        
        // Calculate revenue and order count per month
        Map<String, BigDecimal> revenueByMonth = new HashMap<>();
        Map<String, Long> orderCountByMonth = new HashMap<>();
        
        ordersByMonth.forEach((month, orders) -> {
            BigDecimal monthlyRevenue = orders.stream()
                    .filter(order -> order.getStatus() != Order.OrderStatus.CANCELLED)
                    .map(Order::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            revenueByMonth.put(month, monthlyRevenue);
            orderCountByMonth.put(month, (long) orders.size());
        });
        
        // Group by day for the past 30 days (format: "2023-01-01")
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<Order> last30DaysOrders = recentOrders.stream()
                .filter(order -> order.getCreatedAt().isAfter(thirtyDaysAgo))
                .collect(Collectors.toList());
        
        Map<String, List<Order>> ordersByDay = last30DaysOrders.stream()
                .collect(Collectors.groupingBy(order -> 
                    order.getCreatedAt().getYear() + "-" + 
                    String.format("%02d", order.getCreatedAt().getMonthValue()) + "-" +
                    String.format("%02d", order.getCreatedAt().getDayOfMonth())));
        
        // Calculate revenue and order count per day
        Map<String, BigDecimal> revenueByDay = new HashMap<>();
        Map<String, Long> orderCountByDay = new HashMap<>();
        
        ordersByDay.forEach((day, orders) -> {
            BigDecimal dailyRevenue = orders.stream()
                    .filter(order -> order.getStatus() != Order.OrderStatus.CANCELLED)
                    .map(Order::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            revenueByDay.put(day, dailyRevenue);
            orderCountByDay.put(day, (long) orders.size());
        });
        
        // Set the statistics
        stats.setRevenueByMonth(revenueByMonth);
        stats.setOrdersByMonth(orderCountByMonth);
        stats.setRevenueByDay(revenueByDay);
        stats.setOrdersByDay(orderCountByDay);
    }

    private void calculateUserStatistics(AdminStatisticsDTO stats) {
        // Get counts by role
        long adminCount = userRepository.countByRole(Role.ADMIN);
        long staffCount = userRepository.countByRole(Role.STAFF);
        long userCount = userRepository.countByRole(Role.USER);
        
        // Set total users count
        long totalUsers = adminCount + staffCount + userCount;
        stats.setTotalUsersCount(totalUsers);
        
        // Set user count by role
        Map<String, Long> usersByRole = new HashMap<>();
        usersByRole.put("ADMIN", adminCount);
        usersByRole.put("STAFF", staffCount);
        usersByRole.put("USER", userCount);
        stats.setUsersByRole(usersByRole);
    }

    // Helper methods
    private void updateProductFromDTO(Product product, AdminProductDTO dto) {
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setImageUrl(dto.getImageUrl());
        product.setCategory(dto.getCategory());
        if (dto.getStock() != null) {
            product.setStock(dto.getStock());
        }
    }

    private AdminProductDTO convertToAdminProductDTO(Product product) {
        AdminProductDTO dto = new AdminProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setImageUrl(product.getImageUrl());
        dto.setCategory(product.getCategory());
        dto.setAverageRating(product.getAverageRating());
        dto.setReviewCount(product.getReviewCount());
        dto.setStock(product.getStock());
        return dto;
    }

    private OrderDTO convertToOrderDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setPaymentStatus(order.getPaymentStatus());
        
        // Add user information
        if (order.getUser() != null) {
            dto.setUserId(order.getUser().getId());
            dto.setUserEmail(order.getUser().getEmail());
            dto.setUserName(order.getUser().getName());
            dto.setUserPhoneNumber(order.getUser().getPhoneNumber());
        }

        List<OrderDTO.OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(item -> {
                    OrderDTO.OrderItemDTO itemDTO = new OrderDTO.OrderItemDTO();
                    itemDTO.setProductId(item.getProduct().getId());
                    itemDTO.setProductName(item.getProduct().getName());
                    itemDTO.setProductImage(item.getProduct().getImageUrl());
                    itemDTO.setQuantity(item.getQuantity());
                    itemDTO.setPrice(item.getPrice());
                    
                    // Calculate subtotal
                    BigDecimal subtotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                    itemDTO.setSubtotal(subtotal);
                    
                    return itemDTO;
                })
                .collect(Collectors.toList());
        dto.setItems(itemDTOs);

        return dto;
    }

    private UserDTO convertToUserDTO(User user) {
        return new UserDTO(
            user.getId(), 
            user.getEmail(), 
            user.getName(), 
            user.getAddress(), 
            user.getCity(),
            user.getState(),
            user.getPostalCode(),
            user.getCountry(),
            user.getPhoneNumber(), 
            user.getRole()
        );
    }
    
    private AdminProfileDTO convertToAdminProfileDTO(User user) {
        AdminProfileDTO dto = new AdminProfileDTO();
        
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setAddress(user.getAddress());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole());
        dto.setVerified(user.isVerified());
        dto.setLastLogin(user.getLastLogin());
        dto.setCreatedAt(user.getCreatedAt());
        
        // Calculate statistics for the admin
        long managedUsersCount = userRepository.countByRoleNot(Role.ADMIN);
        dto.setManagedUsersCount((int) managedUsersCount);
        
        // These would be more accurate with proper tracking, but this is a simplified implementation
        // In a real application, you might have fields in Product and Order entities to track the admin who created/processed them
        int createdProductsCount = productRepository.findAll().size();
        dto.setCreatedProductsCount(createdProductsCount);
        
        int processedOrdersCount = orderRepository.findAll().size();
        dto.setProcessedOrdersCount(processedOrdersCount);
        
        return dto;
    }
} 