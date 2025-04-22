/**
 * Razorpay Auto-Verification Integration
 * 
 * This script provides an auto-verification flow for Razorpay
 * Simply include this file and use the initRazorpayPayment function
 */

/**
 * Initialize and open Razorpay payment
 * 
 * @param {Object} orderData - Data from backend razorpay-create-order endpoint
 * @param {Function} onSuccess - Callback on successful payment and verification
 * @param {Function} onError - Callback on payment or verification error
 */
function initRazorpayPayment(orderData, onSuccess, onError) {
  if (!window.Razorpay) {
    onError(new Error('Razorpay SDK not loaded'));
    return;
  }
  
  try {
    // Extract order data
    const razorpayOrderData = orderData.data;
    const options = {
      key: razorpayOrderData.key || orderData.key_id,
      amount: razorpayOrderData.amount,
      currency: razorpayOrderData.currency,
      name: 'Your Store Name',
      description: 'Order Payment',
      order_id: razorpayOrderData.id,
      handler: function(response) {
        console.log('Payment successful, auto-processing...', response);
        
        // Show processing message to user
        if (typeof onSuccess === 'function') {
          onSuccess({
            status: 'processing',
            message: 'Payment received. Processing order...',
            data: response
          });
        }
        
        // Start order status polling
        pollOrderStatus(response, onSuccess, onError);
      },
      modal: {
        ondismiss: function() {
          console.log('Payment canceled by user');
          if (typeof onError === 'function') {
            onError(new Error('Payment canceled'));
          }
        }
      },
      prefill: {
        name: '',
        email: '',
        contact: ''
      },
      notes: {
        auto_verification: true,
        version: '1.0'
      },
      theme: {
        color: '#3399cc'
      }
    };
    
    // Open Razorpay checkout
    const rzp = new window.Razorpay(options);
    rzp.open();
    
    // Handle payment failure
    rzp.on('payment.failed', function(failureResponse) {
      console.error('Payment failed:', failureResponse.error);
      if (typeof onError === 'function') {
        onError(new Error('Payment failed: ' + failureResponse.error.description));
      }
    });
    
  } catch (error) {
    console.error('Error initializing Razorpay:', error);
    if (typeof onError === 'function') {
      onError(error);
    }
  }
}

/**
 * Poll for order status changes
 * 
 * This will repeatedly check the payment status until confirmed or timeout
 */
function pollOrderStatus(paymentResponse, onSuccess, onError, attempt = 0) {
  // Max 10 attempts (30 seconds)
  if (attempt > 10) {
    checkOrderStatusDirectly(paymentResponse, onSuccess, onError);
    return;
  }
  
  setTimeout(() => {
    // Check order status via direct API call
    fetch(`/api/razorpay-payment-status?payment_id=${paymentResponse.razorpay_payment_id}`)
      .then(response => response.json())
      .then(data => {
        console.log('Order status check:', data);
        if (data.status === 'captured' || data.status === 'authorized') {
          // Payment confirmed - notify success
          if (typeof onSuccess === 'function') {
            onSuccess({
              status: 'success',
              message: 'Payment verified successfully.',
              data: data
            });
          }
        } else if (data.status === 'failed') {
          // Payment failed - notify error
          if (typeof onError === 'function') {
            onError(new Error('Payment failed: ' + data.status));
          }
        } else {
          // Still processing, poll again
          pollOrderStatus(paymentResponse, onSuccess, onError, attempt + 1);
        }
      })
      .catch(error => {
        console.error('Error checking payment status:', error);
        // Try again
        pollOrderStatus(paymentResponse, onSuccess, onError, attempt + 1);
      });
  }, 3000); // Check every 3 seconds
}

/**
 * Fallback: check order status directly with backend
 */
function checkOrderStatusDirectly(paymentResponse, onSuccess, onError) {
  try {
    // First try to verify payment directly
    fetch('/api/razorpay-verify-payment', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        razorpay_payment_id: paymentResponse.razorpay_payment_id,
        razorpay_order_id: paymentResponse.razorpay_order_id,
        razorpay_signature: paymentResponse.razorpay_signature
      })
    })
    .then(response => response.json())
    .then(data => {
      if (data.success) {
        // Backend verified the payment
        if (typeof onSuccess === 'function') {
          onSuccess({
            status: 'success',
            message: 'Payment verified successfully after multiple attempts.',
            data: data
          });
        }
      } else {
        // Ask user to check order status manually
        if (typeof onSuccess === 'function') {
          onSuccess({
            status: 'pending',
            message: 'Payment is being processed. Please check your order status.',
            data: paymentResponse
          });
        }
      }
    })
    .catch(error => {
      console.error('Error in direct verification:', error);
      // Still notify as pending since the payment might be processing
      if (typeof onSuccess === 'function') {
        onSuccess({
          status: 'pending',
          message: 'Payment is being processed. Please check your order status.',
          data: paymentResponse
        });
      }
    });
  } catch (error) {
    console.error('Error in direct verification:', error);
    if (typeof onError === 'function') {
      onError(error);
    }
  }
}

/**
 * Example usage (for Angular/React integration):
 * 
 * 1. Include this file in your HTML:
 *    <script src="/js/razorpay-auto-verify.js"></script>
 * 
 * 2. Include Razorpay SDK:
 *    <script src="https://checkout.razorpay.com/v1/checkout.js"></script>
 * 
 * 3. Use in your checkout component:
 * 
 *    // After creating order on your backend
 *    fetch('/api/razorpay-create-order', {
 *      method: 'POST',
 *      headers: { 'Content-Type': 'application/json' },
 *      body: JSON.stringify({ amount: 100, currency: 'INR', receipt: 'order_123' })
 *    })
 *    .then(response => response.json())
 *    .then(orderData => {
 *      // Initialize payment with auto-verification
 *      initRazorpayPayment(
 *        orderData,
 *        (result) => {
 *          // Handle success or processing
 *          if (result.status === 'success') {
 *            // Redirect to success page
 *            window.location.href = '/order-success?orderId=' + orderId;
 *          } else {
 *            // Show processing message
 *            showProcessingMessage(result.message);
 *          }
 *        },
 *        (error) => {
 *          // Handle error
 *          showErrorMessage('Payment failed: ' + error.message);
 *        }
 *      );
 *    });
 */ 