/**
 * Razorpay Helper Functions
 * 
 * This file contains helper functions for integrating Razorpay in the frontend.
 */

/**
 * Configure Razorpay options with appropriate error handlers
 * 
 * @param {Object} options - Base Razorpay options
 * @returns {Object} Enhanced Razorpay options with error handlers
 */
function configureRazorpayOptions(options) {
    // Clone the options to avoid modifying the original
    const enhancedOptions = { ...options };
    
    // Add custom handler for payment errors
    const originalHandler = enhancedOptions.handler || function() {};
    const originalErrorHandler = enhancedOptions.modal?.ondismiss || function() {};
    
    // Override the handler
    enhancedOptions.handler = function(response) {
        console.log('Payment successful', response);
        
        // First try to verify the payment manually
        verifyPaymentManually(response)
            .catch(error => {
                console.log('Manual verification failed, waiting for webhook auto-verification', error);
                // If manual verification fails, it's okay
                // The webhook will handle it automatically
                showProcessingMessage();
            })
            .finally(() => {
                // Call the original handler to handle UI updates
                originalHandler(response);
            });
    };
    
    // Handle payment failures
    enhancedOptions.modal = {
        ...enhancedOptions.modal,
        ondismiss: function() {
            console.log('Checkout form closed');
            originalErrorHandler();
        }
    };
    
    // Custom error handler
    enhancedOptions.on = {
        ...enhancedOptions.on,
        payment: {
            failed: function(response) {
                handlePaymentFailure(response.error);
            }
        }
    };
    
    return enhancedOptions;
}

/**
 * Verify payment manually (as a backup to automatic webhook verification)
 *
 * @param {Object} response - Razorpay response containing payment details
 * @returns {Promise} Promise that resolves when verification is complete
 */
function verifyPaymentManually(response) {
    return new Promise((resolve, reject) => {
        fetch('/api/razorpay-verify-payment', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                razorpay_payment_id: response.razorpay_payment_id,
                razorpay_order_id: response.razorpay_order_id,
                razorpay_signature: response.razorpay_signature
            })
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                console.log('Payment verified successfully (manual)');
                resolve(data);
            } else {
                console.error('Payment verification failed (manual)');
                reject(new Error('Payment verification failed (manual)'));
            }
        })
        .catch(error => {
            console.error('Error verifying payment (manual):', error);
            reject(error);
        });
    });
}

/**
 * Show a processing message to the user while webhook verification is in progress
 */
function showProcessingMessage() {
    // Example implementation - replace with your actual UI code
    const processingElement = document.getElementById('payment-processing-message');
    if (processingElement) {
        processingElement.textContent = 'Processing your payment. Please wait...';
        processingElement.style.display = 'block';
    }
}

/**
 * Handle Razorpay payment failures with descriptive messages
 * 
 * @param {Object} error - Error object from Razorpay
 */
function handlePaymentFailure(error) {
    console.error('Payment failed:', error);
    
    // Map error codes to user-friendly messages
    const errorMessages = {
        BAD_REQUEST_ERROR: {
            international_transaction_not_allowed: 
                'International cards are not supported. Please use an Indian card, UPI, or netbanking.',
            default: 'There was an issue with your payment request. Please try again with a different payment method.'
        },
        GATEWAY_ERROR: {
            default: 'The payment gateway encountered an error. Please try again or use a different payment method.'
        },
        NETWORK_ERROR: {
            default: 'Network error occurred during payment. Please check your internet connection and try again.'
        },
        default: 'Payment failed. Please try again with a different payment method.'
    };
    
    // Get appropriate error message
    let message;
    if (errorMessages[error.code]) {
        message = errorMessages[error.code][error.reason] || errorMessages[error.code].default;
    } else {
        message = errorMessages.default;
    }
    
    // Show error to user (implement your own UI for this)
    showPaymentError(message, error);
}

/**
 * Display payment error to user
 * Can be implemented based on your UI framework
 * 
 * @param {string} message - User-friendly error message
 * @param {Object} error - Original error object for debugging
 */
function showPaymentError(message, error) {
    // Example implementation - replace with your actual UI code
    alert(message);
    
    // You can also update a specific error element in your UI
    const errorElement = document.getElementById('payment-error-message');
    if (errorElement) {
        errorElement.textContent = message;
        errorElement.style.display = 'block';
    }
    
    // Log detailed error for debugging
    console.error('Payment error details:', error);
}

/**
 * Get recommended payment methods based on previous failures
 * 
 * @param {Object} error - Previous error from Razorpay
 * @returns {Array} Recommended payment methods
 */
function getRecommendedPaymentMethods(error) {
    // Default recommendations
    let recommendations = ['UPI', 'Netbanking', 'Wallet'];
    
    // Customize based on previous error
    if (error && error.code === 'BAD_REQUEST_ERROR' && error.reason === 'international_transaction_not_allowed') {
        recommendations = ['UPI', 'Netbanking', 'Domestic Card'];
    }
    
    return recommendations;
}

/**
 * Get test payment information for development
 */
function getTestPaymentInfo() {
    return {
        testCards: {
            domestic: '4111 1111 1111 1111',
            expiry: 'Any future date',
            cvv: 'Any 3 digits'
        },
        testUpi: 'success@razorpay',
        testUpiFailure: 'failure@razorpay',
        note: 'For testing, UPI is the most reliable payment method.'
    };
} 