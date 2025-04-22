/**
 * Checkout Component Guide for Razorpay Auto-Verification
 * 
 * This file contains code samples to implement auto-verification in your Angular/React frontend
 */

/**
 * Angular Example
 * 
 * Update your checkout.component.ts to implement auto-verification
 */

// --- Angular Example ---

/**
 * Initiate Razorpay payment with auto-verification
 * @param orderId The order ID
 */
initiatePayment(orderId: string): void {
  this.orderService.createRazorpayOrder(orderId).subscribe({
    next: (response) => {
      if (response.success) {
        const razorpayOrderData = response.data;
        
        // Configure the options using our helper
        const options = {
          key: razorpayOrderData.key_id || environment.razorpayKeyId,
          amount: razorpayOrderData.amount,
          currency: razorpayOrderData.currency,
          name: 'Your Store Name',
          description: 'Payment for order #' + orderId,
          order_id: razorpayOrderData.id,
          handler: (response) => {
            // The payment will be auto-verified by webhook
            // This handler is just for UI updates
            this.handleSuccessfulPayment(orderId, response);
          }
        };
        
        // Open Razorpay checkout
        const rzp = new Razorpay(options);
        rzp.open();
        
        // Handle payment modal closing
        rzp.on('payment.failed', (response) => {
          this.handlePaymentFailure(response.error);
        });
      }
    },
    error: (error) => {
      console.error('Error initiating payment:', error);
      this.showError('Failed to initiate payment. Please try again.');
    }
  });
}

/**
 * Handle successful payment response from Razorpay
 */
handleSuccessfulPayment(orderId: string, response: any): void {
  // Show processing message
  this.showProcessingMessage();
  
  // First try manual verification (as backup)
  this.razorpayService.verifyPayment(
    response.razorpay_payment_id,
    response.razorpay_order_id,
    response.razorpay_signature
  ).subscribe({
    next: (verificationResponse) => {
      if (verificationResponse.success) {
        // Manual verification successful
        this.router.navigate(['/order-success'], { 
          queryParams: { orderId: orderId } 
        });
      } else {
        // Manual verification failed, but webhook might succeed
        this.handlePendingVerification(orderId);
      }
    },
    error: (error) => {
      // Manual verification error, but webhook might succeed
      console.error('Manual verification error:', error);
      this.handlePendingVerification(orderId);
    }
  });
}

/**
 * Handle pending verification - wait for webhook to process
 */
handlePendingVerification(orderId: string): void {
  // Show message that payment is being processed
  this.showWaitingForVerificationMessage();
  
  // Start polling for order status
  this.pollOrderStatus(orderId);
}

/**
 * Poll for order status changes
 */
pollOrderStatus(orderId: string, attempts = 0): void {
  if (attempts > 5) {
    // Stop polling after 5 attempts
    this.router.navigate(['/order-pending'], { 
      queryParams: { orderId: orderId, status: 'verification-pending' } 
    });
    return;
  }
  
  setTimeout(() => {
    this.orderService.getOrderStatus(orderId).subscribe({
      next: (orderData) => {
        if (orderData.paymentStatus === 'COMPLETED') {
          // Payment was verified by webhook
          this.router.navigate(['/order-success'], { 
            queryParams: { orderId: orderId } 
          });
        } else {
          // Continue polling
          this.pollOrderStatus(orderId, attempts + 1);
        }
      },
      error: (error) => {
        console.error('Error checking order status:', error);
        this.pollOrderStatus(orderId, attempts + 1);
      }
    });
  }, 3000); // Poll every 3 seconds
}

/**
 * React Example
 * 
 * Update your checkout component to implement auto-verification
 */

// --- React Example ---

import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function CheckoutComponent() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [processingMessage, setProcessingMessage] = useState('');
  const navigate = useNavigate();
  
  // Function to initiate payment
  const initiatePayment = async (orderId) => {
    try {
      setLoading(true);
      const response = await orderService.createRazorpayOrder(orderId);
      
      if (response.success) {
        const razorpayOrderData = response.data;
        
        const options = {
          key: razorpayOrderData.key_id,
          amount: razorpayOrderData.amount,
          currency: razorpayOrderData.currency,
          name: 'Your Store Name',
          description: 'Payment for order #' + orderId,
          order_id: razorpayOrderData.id,
          handler: (response) => {
            // The payment will be auto-verified by webhook
            // This handler is just for UI updates
            handleSuccessfulPayment(orderId, response);
          },
          modal: {
            ondismiss: () => {
              console.log('Payment modal closed');
              setLoading(false);
            }
          }
        };
        
        const rzp = new window.Razorpay(options);
        rzp.open();
        
        rzp.on('payment.failed', (response) => {
          handlePaymentFailure(response.error);
        });
      }
    } catch (error) {
      console.error('Error initiating payment:', error);
      setError('Failed to initiate payment. Please try again.');
      setLoading(false);
    }
  };
  
  // Handle successful payment
  const handleSuccessfulPayment = async (orderId, response) => {
    setProcessingMessage('Processing your payment...');
    
    try {
      // First try manual verification (as backup)
      const verificationResponse = await razorpayService.verifyPayment(
        response.razorpay_payment_id,
        response.razorpay_order_id,
        response.razorpay_signature
      );
      
      if (verificationResponse.success) {
        // Manual verification successful
        navigate(`/order-success?orderId=${orderId}`);
      } else {
        // Manual verification failed, but webhook might succeed
        handlePendingVerification(orderId);
      }
    } catch (error) {
      // Manual verification error, but webhook might succeed
      console.error('Manual verification error:', error);
      handlePendingVerification(orderId);
    }
  };
  
  // Handle pending verification
  const handlePendingVerification = (orderId) => {
    setProcessingMessage('Verifying your payment. Please wait a moment...');
    pollOrderStatus(orderId);
  };
  
  // Poll for order status
  const pollOrderStatus = async (orderId, attempts = 0) => {
    if (attempts > 5) {
      navigate(`/order-pending?orderId=${orderId}&status=verification-pending`);
      return;
    }
    
    try {
      const orderData = await orderService.getOrderStatus(orderId);
      
      if (orderData.paymentStatus === 'COMPLETED') {
        // Payment was verified by webhook
        navigate(`/order-success?orderId=${orderId}`);
      } else {
        // Continue polling
        setTimeout(() => {
          pollOrderStatus(orderId, attempts + 1);
        }, 3000);
      }
    } catch (error) {
      console.error('Error checking order status:', error);
      setTimeout(() => {
        pollOrderStatus(orderId, attempts + 1);
      }, 3000);
    }
  };
  
  return (
    <div>
      {/* Your checkout component UI */}
      {processingMessage && (
        <div className="processing-message">{processingMessage}</div>
      )}
      {error && (
        <div className="error-message">{error}</div>
      )}
      <button 
        onClick={() => initiatePayment('order123')}
        disabled={loading}
      >
        {loading ? 'Processing...' : 'Pay Now'}
      </button>
    </div>
  );
}

export default CheckoutComponent; 