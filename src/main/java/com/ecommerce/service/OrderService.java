package com.ecommerce.service;

import com.ecommerce.dto.OrderDTO;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderItem;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.User;
import com.ecommerce.entity.Product;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RazorpayService razorpayService;
    
    @Autowired
    private ProductRepository productRepository;

    public List<OrderDTO> getOrders() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO getOrder(Long orderId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Not authorized to view this order");
        }

        return convertToDTO(order);
    }

    @Transactional
    public OrderDTO placeOrder(String shippingAddress) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Cart> cartItems = cartRepository.findByUserId(user.getId());
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Check if there's enough stock for each product
        for (Cart cartItem : cartItems) {
            Product product = cartItem.getProduct();
            Integer currentStock = product.getStock();
            Integer orderedQuantity = cartItem.getQuantity();
            
            if (currentStock < orderedQuantity) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(Order.OrderStatus.PENDING);
        order.setShippingAddress(shippingAddress);
        order.setPaymentStatus("PENDING");

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Cart cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            order.getItems().add(orderItem);
            
            totalAmount = totalAmount.add(cartItem.getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            
            // Reduce the product stock
            Product product = cartItem.getProduct();
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }
        order.setTotalAmount(totalAmount);

        order = orderRepository.save(order);
        cartRepository.deleteAll(cartItems);

        return convertToDTO(order);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Not authorized to cancel this order");
        }

        // Allow cancellation only if order is not already delivered or cancelled
        if (order.getStatus() == Order.OrderStatus.DELIVERED || order.getStatus() == Order.OrderStatus.CANCELLED) {
            throw new RuntimeException("Cannot cancel order in current status: " + order.getStatus());
        }

        // Return the stock for each product
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            Integer newStock = product.getStock() + item.getQuantity();
            product.setStock(newStock);
            productRepository.save(product);
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    @Transactional
    public void updatePaymentStatus(String orderId, String paymentId, String signature) {
        boolean isValid = razorpayService.verifyPaymentSignature(orderId, paymentId, signature);
        
        if (isValid) {
            // Extract the numeric order ID from receipt
            String[] parts = orderId.split("_");
            if (parts.length > 1) {
                try {
                    Long orderIdNumeric = Long.parseLong(parts[1]);
                    Order order = orderRepository.findById(orderIdNumeric)
                            .orElseThrow(() -> new RuntimeException("Order not found"));
                    
                    order.setPaymentStatus("COMPLETED");
                    order.setStatus(Order.OrderStatus.CONFIRMED);
                    
                    // Stock reduction is now handled at order placement
                    
                    orderRepository.save(order);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Invalid order ID format");
                }
            } else {
                throw new RuntimeException("Invalid order ID format");
            }
        } else {
            throw new RuntimeException("Payment verification failed");
        }
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setPaymentStatus(order.getPaymentStatus());

        List<OrderDTO.OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(item -> {
                    OrderDTO.OrderItemDTO itemDTO = new OrderDTO.OrderItemDTO();
                    itemDTO.setProductId(item.getProduct().getId());
                    itemDTO.setProductName(item.getProduct().getName());
                    itemDTO.setProductImage(item.getProduct().getImageUrl());
                    itemDTO.setQuantity(item.getQuantity());
                    itemDTO.setPrice(item.getPrice());
                    return itemDTO;
                })
                .collect(Collectors.toList());
        dto.setItems(itemDTOs);

        return dto;
    }
    
    /**
     * Checks if an order can be cancelled based on its current status
     * @param orderId The ID of the order to check
     * @return true if the order can be cancelled, false otherwise
     */
    public boolean canCancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
                
        // Orders can be cancelled if they are not already delivered or cancelled
        return order.getStatus() != Order.OrderStatus.DELIVERED 
               && order.getStatus() != Order.OrderStatus.CANCELLED;
    }
} 