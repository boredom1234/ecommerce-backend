package com.ecommerce.controller;

import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dto.OrderRequest;
import com.ecommerce.dto.PaymentVerificationRequest;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.RazorpayService;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import jakarta.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
public class RazorpayController {

    private static final Logger logger = LoggerFactory.getLogger(RazorpayController.class);

    @Autowired
    private RazorpayService razorpayService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private RazorpayClient razorpayClient;
    
    @Value("${razorpay.key_id}")
    private String razorpayKeyId;
    
    @Value("${razorpay.webhook_secret}")
    private String webhookSecret;
    
    @GetMapping("/razorpay-test")
    public ResponseEntity<?> testRazorpay() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Razorpay integration is working");
        response.put("key_id", razorpayKeyId);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/razorpay-payment-info")
    public ResponseEntity<?> getPaymentInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("key_id", razorpayKeyId);
        
        // Add payment method support information
        Map<String, Object> supportInfo = new HashMap<>();
        supportInfo.put("international_cards", false);
        supportInfo.put("upi", true);
        supportInfo.put("netbanking", true);
        supportInfo.put("wallet", true);
        supportInfo.put("domestic_cards", true);
        
        // Add test card information
        Map<String, Object> testCards = new HashMap<>();
        testCards.put("domestic_success", "4111 1111 1111 1111");
        testCards.put("domestic_failure", "4111 1111 1111 1111");
        testCards.put("expiry", "Any future date");
        testCards.put("cvv", "Any 3 digits");
        testCards.put("name", "Any name");
        
        response.put("supported_methods", supportInfo);
        response.put("test_cards", testCards);
        response.put("payment_notes", "For testing, use UPI option with success@razorpay for successful payments or failure@razorpay for failed payments");
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/razorpay-create-order")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            logger.info("Creating Razorpay order with amount: {}, currency: {}, receipt: {}", 
                    orderRequest.getAmount(), orderRequest.getCurrency(), orderRequest.getReceipt());
            
            // Convert amount to paise (smallest unit)
            int amountInPaise = (int)(orderRequest.getAmount() * 100);
            
            Order order = razorpayService.createOrder(
                amountInPaise, 
                orderRequest.getCurrency(), 
                orderRequest.getReceipt()
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", order);
            
            // Add payment method support information
            Map<String, Object> supportInfo = new HashMap<>();
            supportInfo.put("international_cards", false);
            supportInfo.put("upi", true);
            supportInfo.put("netbanking", true);
            supportInfo.put("wallet", true);
            supportInfo.put("domestic_cards", true);
            
            response.put("supported_methods", supportInfo);
            response.put("payment_notes", "For testing, use UPI option with success@razorpay for successful payments");
            
            return ResponseEntity.ok(response);
        } catch (RazorpayException e) {
            logger.error("Error creating Razorpay order", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @PostMapping("/razorpay-verify-payment")
    public ResponseEntity<?> verifyPayment(@RequestBody PaymentVerificationRequest request) {
        try {
            logger.info("Received payment verification request for paymentId: {}", 
                    request.getRazorpay_payment_id());
            
            Map<String, Object> response = new HashMap<>();
            
            // Validate request parameters
            if (request.getRazorpay_payment_id() == null) {
                logger.error("Missing razorpay_payment_id in verification request");
                response.put("success", false);
                response.put("error", "Missing payment ID");
                return ResponseEntity.badRequest().body(response);
            }
            
            // If signature is missing, we can attempt to fetch it for auto-verification
            if (request.getRazorpay_signature() == null && request.getRazorpay_order_id() != null) {
                logger.info("Signature missing, attempting to fetch it from Razorpay");
                String signature = razorpayService.fetchPaymentSignature(
                    request.getRazorpay_payment_id(), 
                    request.getRazorpay_order_id()
                );
                
                if (signature != null) {
                    // Use the fetched signature
                    logger.info("Auto-generated signature for verification");
                    request.setRazorpay_signature(signature);
                } else {
                    logger.error("Could not fetch signature for payment");
                    response.put("success", false);
                    response.put("error", "Missing signature and could not auto-generate one");
                    return ResponseEntity.badRequest().body(response);
                }
            }
            
            // Now verify the payment
            boolean isValidSignature = razorpayService.verifyPaymentSignature(
                request.getRazorpay_order_id(),
                request.getRazorpay_payment_id(),
                request.getRazorpay_signature()
            );
            
            response.put("success", isValidSignature);
            
            if (!isValidSignature) {
                logger.error("Invalid payment signature");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Update order status in database
            try {
                orderService.updatePaymentStatus(
                    request.getRazorpay_order_id(),
                    request.getRazorpay_payment_id(),
                    request.getRazorpay_signature()
                );
                logger.info("Order payment status updated successfully");
                response.put("order_status_update", true);
            } catch (Exception e) {
                logger.error("Error updating order payment status", e);
                response.put("order_status_update", false);
                response.put("order_status_message", e.getMessage());
                // Continue execution even if order update fails
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error verifying payment", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @PostMapping("/razorpay-webhook")
    public ResponseEntity<?> handleWebhook(HttpServletRequest request) {
        try {
            // Read the request body
            StringBuilder buffer = new StringBuilder();
            String line;
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            String payload = buffer.toString();
            
            // Log the payload for debugging purposes
            logger.debug("Webhook payload: {}", payload);
            
            // Get the signature from header
            String razorpaySignature = request.getHeader("X-Razorpay-Signature");
            logger.debug("Webhook signature header: {}", razorpaySignature);
            
            // Verify webhook signature
            boolean isValidSignature = razorpayService.verifyWebhookSignature(payload, razorpaySignature, webhookSecret);
            
            if (!isValidSignature) {
                logger.error("Invalid webhook signature");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            // Process the webhook payload
            JSONObject payloadJson = new JSONObject(payload);
            String event = payloadJson.getString("event");
            
            logger.info("Received Razorpay webhook: {}", event);
            
            // Handle payment.authorized event
            if ("payment.authorized".equals(event)) {
                JSONObject payment = payloadJson.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity");
                
                String paymentId = payment.getString("id");
                String orderId = payment.getString("order_id");
                
                logger.info("Processing authorized payment - paymentId: {}, orderId: {}", paymentId, orderId);
                
                // Get payment signature from Razorpay API
                String signature = razorpayService.fetchPaymentSignature(paymentId, orderId);
                
                if (signature != null) {
                    // Update order status in database
                    try {
                        orderService.updatePaymentStatus(orderId, paymentId, signature);
                        logger.info("Order payment updated via webhook for orderId: {}", orderId);
                    } catch (Exception e) {
                        logger.error("Error updating order payment status via webhook", e);
                    }
                }
            }
            
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error processing Razorpay webhook", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/razorpay-payment-status")
    public ResponseEntity<?> getPaymentStatus(@RequestParam("payment_id") String paymentId) {
        try {
            logger.info("Checking payment status for paymentId: {}", paymentId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("payment_id", paymentId);
            
            try {
                // Fetch payment details from Razorpay
                Payment payment = razorpayClient.payments.fetch(paymentId);
                String orderId = payment.get("order_id");
                String status = payment.get("status");
                
                response.put("order_id", orderId);
                response.put("status", status);
                response.put("payment_details", payment);
                
                logger.info("Payment status for {} is: {}", paymentId, status);
                
                return ResponseEntity.ok(response);
            } catch (RazorpayException e) {
                logger.error("Error fetching payment from Razorpay", e);
                response.put("error", "Could not fetch payment details: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            logger.error("Error checking payment status", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
} 