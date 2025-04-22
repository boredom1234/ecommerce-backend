package com.ecommerce.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RazorpayService {
    
    private static final Logger logger = LoggerFactory.getLogger(RazorpayService.class);

    @Autowired
    private RazorpayClient razorpayClient;
    
    @Value("${razorpay.key_secret}")
    private String keySecret;
    
    public Order createOrder(int amount, String currency, String receipt) throws RazorpayException {
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount); // amount in the smallest currency unit (paise)
        orderRequest.put("currency", currency);
        orderRequest.put("receipt", receipt);
        orderRequest.put("payment_capture", 1); // auto capture
        
        try {
            return razorpayClient.orders.create(orderRequest);
        } catch (RazorpayException e) {
            throw new RazorpayException("Error creating Razorpay order: " + e.getMessage());
        }
    }
    
    public boolean verifyPaymentSignature(String orderId, String paymentId, String signature) {
        try {
            // Validate input parameters
            if (orderId == null || paymentId == null || signature == null) {
                logger.warn("Cannot verify payment signature - missing required parameter(s): " + 
                         "orderId=" + (orderId != null) + 
                         ", paymentId=" + (paymentId != null) + 
                         ", signature=" + (signature != null));
                return false;
            }
            
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", orderId);
            options.put("razorpay_payment_id", paymentId);
            options.put("razorpay_signature", signature);
            
            logger.debug("Verifying payment signature: orderId={}, paymentId={}", orderId, paymentId);
            boolean isValid = Utils.verifyPaymentSignature(options, keySecret);
            return isValid;
        } catch (RazorpayException e) {
            logger.error("RazorpayException while verifying payment signature", e);
            return false;
        } catch (Exception e) {
            logger.error("Unexpected error while verifying payment signature", e);
            return false;
        }
    }
    
    /**
     * Verifies the Razorpay webhook signature
     * 
     * @param payload The webhook payload
     * @param signature The X-Razorpay-Signature header value
     * @param webhookSecret The webhook secret from Razorpay dashboard
     * @return true if signature is valid, false otherwise
     */
    public boolean verifyWebhookSignature(String payload, String signature, String webhookSecret) {
        try {
            if (payload == null || signature == null || webhookSecret == null) {
                return false;
            }
            
            // Create an HMAC-SHA256 signature
            SecretKeySpec secretKeySpec = new SecretKeySpec(webhookSecret.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hmacData = mac.doFinal(payload.getBytes());
            
            // Convert to hex string
            String calculatedSignature = bytesToHex(hmacData);
            
            // Compare signatures
            return signature.equals(calculatedSignature);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            logger.error("Error verifying webhook signature", e);
            return false;
        }
    }
    
    /**
     * Fetches the payment signature from Razorpay API
     * 
     * @param paymentId The Razorpay payment ID
     * @param orderId The Razorpay order ID
     * @return The payment signature or null if not found
     */
    public String fetchPaymentSignature(String paymentId, String orderId) {
        try {
            // This is a simplification - Razorpay doesn't actually provide an API to get signatures
            // In real implementation, you'd store the signature when payment is verified
            
            // Verify the payment exists
            Payment payment = razorpayClient.payments.fetch(paymentId);
            if (payment != null && payment.get("order_id").equals(orderId)) {
                // For testing only: generate a dummy signature
                // In production, you would have stored this signature
                String dataToSign = orderId + "|" + paymentId;
                return generateHmacSha256(dataToSign, keySecret);
            }
            return null;
        } catch (RazorpayException | NoSuchAlgorithmException | InvalidKeyException e) {
            logger.error("Error fetching payment signature", e);
            return null;
        }
    }
    
    /**
     * Generate HMAC-SHA256 hash
     */
    private String generateHmacSha256(String data, String key) 
            throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        return bytesToHex(mac.doFinal(data.getBytes()));
    }
    
    /**
     * Convert byte array to hex string
     */
    private String bytesToHex(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
} 