package com.ecommerce.controller;

import com.ecommerce.dto.OrderDTO;
import com.ecommerce.dto.ApiResponse;
import com.ecommerce.dto.OrderRequest;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.RazorpayService;
import com.razorpay.Order;
import com.razorpay.RazorpayException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
public class OrderController {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private RazorpayService razorpayService;

    @GetMapping("/orders")
    public ResponseEntity<?> getOrders() {
        List<OrderDTO> orders = orderService.getOrders();
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Orders retrieved successfully", orders));
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<?> getOrder(@PathVariable Long id) {
        OrderDTO order = orderService.getOrder(id);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Order retrieved successfully", order));
    }

    @PostMapping("/order-place-debug-raw")
    public ResponseEntity<?> debugRawRequestBody(HttpServletRequest request) {
        try {
            StringBuilder buffer = new StringBuilder();
            String line;
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            String payload = buffer.toString();
            
            logger.info("Raw request payload: {}", payload);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Received payload");
            response.put("payload", payload);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error reading request payload", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error reading request payload: " + e.getMessage(), null));
        }
    }

    @PostMapping("/order-place")
    public ResponseEntity<?> placeOrder(@RequestBody(required = false) Map<String, Object> rawRequest) {
        logger.info("Received raw order request: {}", rawRequest);
        
        if (rawRequest == null) {
            logger.error("Invalid order request: request is null");
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Order request is required", null));
        }
        
        // Extract shipping address from the raw request
        String shippingAddress = extractShippingAddress(rawRequest);
        
        if (shippingAddress == null || shippingAddress.isEmpty()) {
            logger.error("Invalid order request: shipping address is missing");
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Shipping address is required", null));
        }
        
        OrderDTO order = orderService.placeOrder(shippingAddress);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Order placed successfully", order));
    }
    
    @SuppressWarnings("unchecked")
    private String extractShippingAddress(Map<String, Object> rawRequest) {
        // First check if shippingAddress is a string
        if (rawRequest.containsKey("shippingAddress") && rawRequest.get("shippingAddress") instanceof String) {
            return (String) rawRequest.get("shippingAddress");
        }
        
        // Then check if shippingAddress is an object
        if (rawRequest.containsKey("shippingAddress") && rawRequest.get("shippingAddress") instanceof Map) {
            Map<String, Object> addressMap = (Map<String, Object>) rawRequest.get("shippingAddress");
            return formatAddressFromMap(addressMap);
        }
        
        // Then check if address is an object
        if (rawRequest.containsKey("address") && rawRequest.get("address") instanceof Map) {
            Map<String, Object> addressMap = (Map<String, Object>) rawRequest.get("address");
            return formatAddressFromMap(addressMap);
        }
        
        return null;
    }
    
    private String formatAddressFromMap(Map<String, Object> addressMap) {
        StringBuilder sb = new StringBuilder();
        
        appendIfExists(sb, addressMap, "street");
        appendIfExists(sb, addressMap, "city");
        appendIfExists(sb, addressMap, "state");
        appendIfExists(sb, addressMap, "zipCode");
        appendIfExists(sb, addressMap, "zip"); // Alternative field name
        appendIfExists(sb, addressMap, "country");
        
        String result = sb.toString();
        // Remove trailing comma and space if present
        if (result.endsWith(", ")) {
            result = result.substring(0, result.length() - 2);
        }
        
        return result;
    }
    
    private void appendIfExists(StringBuilder sb, Map<String, Object> map, String key) {
        if (map.containsKey(key) && map.get(key) != null && !map.get(key).toString().isEmpty()) {
            sb.append(map.get(key)).append(", ");
        }
    }
    
    // Alternative endpoint to support form data submission
    @PostMapping("/order-place-form")
    public ResponseEntity<?> placeOrderWithFormData(@RequestParam String shippingAddress) {
        logger.debug("Received order request with form data: {}", shippingAddress);
        
        if (shippingAddress == null || shippingAddress.isEmpty()) {
            logger.error("Invalid order request: shipping address is missing");
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Shipping address is required", null));
        }
        
        OrderDTO order = orderService.placeOrder(shippingAddress);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Order placed successfully", order));
    }
    
    @PostMapping("/order-place-debug")
    public ResponseEntity<?> placeOrderDebug(HttpServletRequest request) {
        try {
            // Read the raw request body
            StringBuilder requestBody = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            
            // Log the raw request body
            logger.debug("Raw request body: {}", requestBody.toString());
            
            // Get the content type
            String contentType = request.getContentType();
            logger.debug("Content-Type: {}", contentType);
            
            // Get all headers
            Map<String, String> headers = new HashMap<>();
            java.util.Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                headers.put(headerName, request.getHeader(headerName));
            }
            logger.debug("Headers: {}", headers);
            
            return ResponseEntity.ok()
                    .body(new ApiResponse<>(true, "Debug information logged", null));
        } catch (IOException e) {
            logger.error("Error reading request body", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error reading request body", null));
        }
    }
    
    @PostMapping("/order/{id}/initiate-payment")
    public ResponseEntity<?> initiatePayment(@PathVariable Long id) {
        try {
            // Get order details
            OrderDTO orderDTO = orderService.getOrder(id);
            
            // Prepare the order request
            OrderRequest razorpayOrderRequest = new OrderRequest();
            razorpayOrderRequest.setAmount(orderDTO.getTotalAmount().doubleValue());
            razorpayOrderRequest.setCurrency("INR");
            razorpayOrderRequest.setReceipt("order_rcptid_" + id);
            
            // Create Razorpay order
            int amountInPaise = (int)(orderDTO.getTotalAmount().doubleValue() * 100);
            Order razorpayOrder = razorpayService.createOrder(
                amountInPaise,
                "INR",
                "order_rcptid_" + id
            );
            
            // Prepare response
            Map<String, Object> response = new HashMap<>();
            response.put("orderId", razorpayOrder.get("id"));
            response.put("amount", razorpayOrder.get("amount"));
            response.put("currency", razorpayOrder.get("currency"));
            response.put("receipt", razorpayOrder.get("receipt"));
            
            return ResponseEntity.ok()
                    .body(new ApiResponse<>(true, "Payment initiated successfully", response));
        } catch (RazorpayException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Failed to initiate payment: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/order-delete/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Order cancelled successfully", null));
    }
    
    @DeleteMapping("/order/{id}/cancel")
    public ResponseEntity<?> cancelOrderEndpoint(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Order cancelled successfully", null));
    }
    
    @GetMapping("/order/{id}/can-cancel")
    public ResponseEntity<?> canCancelOrder(@PathVariable Long id) {
        boolean canCancel = orderService.canCancelOrder(id);
        Map<String, Object> response = new HashMap<>();
        response.put("canCancel", canCancel);
        
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Order cancellation status retrieved", response));
    }
} 