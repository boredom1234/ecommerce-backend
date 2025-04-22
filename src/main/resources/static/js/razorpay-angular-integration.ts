/**
 * Razorpay Auto-Verification Angular Integration
 * 
 * This file provides example code for integrating the auto-verification flow in Angular
 */

// ============================================================================
// Example RazorpayService (razorpay.service.ts)
// ============================================================================

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

declare var initRazorpayPayment: any;

@Injectable({
  providedIn: 'root'
})
export class RazorpayService {
  private apiUrl = 'http://localhost:5000/api';

  constructor(private http: HttpClient) {}

  /**
   * Create a new Razorpay order
   */
  createOrder(orderData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/razorpay-create-order`, orderData)
      .pipe(
        catchError(error => {
          console.error('Error creating order:', error);
          return throwError(() => new Error('Failed to create order: ' + error.message));
        })
      );
  }

  /**
   * Get order information
   */
  getOrder(orderId: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/order/${orderId}`)
      .pipe(
        catchError(error => {
          console.error('Error getting order:', error);
          return throwError(() => new Error('Failed to get order details: ' + error.message));
        })
      );
  }

  /**
   * Initialize payment with auto-verification
   * 
   * This method uses the razorpay-auto-verify.js script to handle the payment flow
   */
  initiatePaymentWithAutoVerification(orderData: any): Promise<any> {
    return new Promise((resolve, reject) => {
      if (typeof initRazorpayPayment !== 'function') {
        reject(new Error('Razorpay auto-verification script not loaded'));
        return;
      }

      initRazorpayPayment(
        orderData,
        (result: any) => {
          // Success or processing
          resolve(result);
        },
        (error: any) => {
          // Error
          reject(error);
        }
      );
    });
  }
}


// ============================================================================
// Example Checkout Component (checkout.component.ts)
// ============================================================================

import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RazorpayService } from '../services/razorpay.service';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.scss']
})
export class CheckoutComponent implements OnInit {
  loading = false;
  error = '';
  processingMessage = '';
  orderDetails: any = null;
  
  constructor(
    private razorpayService: RazorpayService,
    private router: Router
  ) { }

  ngOnInit(): void {
    // Load order details or other initialization
  }

  /**
   * Process payment for the current order
   */
  processPayment(orderId: string): void {
    this.loading = true;
    this.error = '';
    this.processingMessage = 'Initializing payment...';
    
    // 1. Get order details
    this.razorpayService.getOrder(orderId).subscribe({
      next: (orderData) => {
        this.orderDetails = orderData;
        
        // 2. Create Razorpay order
        const razorpayOrderRequest = {
          amount: orderData.totalAmount,
          currency: 'INR',
          receipt: `order_${orderId}`
        };
        
        this.razorpayService.createOrder(razorpayOrderRequest).subscribe({
          next: (razorpayOrderData) => {
            // 3. Initialize payment with auto-verification
            this.initializeRazorpayPayment(razorpayOrderData, orderId);
          },
          error: (error) => {
            this.loading = false;
            this.error = `Failed to create payment: ${error.message}`;
            console.error('Order creation error:', error);
          }
        });
      },
      error: (error) => {
        this.loading = false;
        this.error = `Failed to load order: ${error.message}`;
        console.error('Order loading error:', error);
      }
    });
  }

  /**
   * Initialize Razorpay payment with auto-verification
   */
  private initializeRazorpayPayment(razorpayOrderData: any, orderId: string): void {
    this.razorpayService.initiatePaymentWithAutoVerification(razorpayOrderData)
      .then((result) => {
        // Handle the payment result
        if (result.status === 'success') {
          // Payment succeeded and was verified
          this.router.navigate(['/order-success'], { 
            queryParams: { orderId: orderId }
          });
        } else if (result.status === 'processing') {
          // Payment is being processed
          this.processingMessage = result.message;
        } else if (result.status === 'pending') {
          // Payment needs manual verification
          this.router.navigate(['/order-pending'], {
            queryParams: { orderId: orderId, status: 'verification-pending' }
          });
        }
      })
      .catch((error) => {
        this.loading = false;
        this.error = `Payment failed: ${error.message}`;
        console.error('Payment error:', error);
      });
  }
}


// ============================================================================
// Example HTML Template (checkout.component.html)
// ============================================================================

<!--
<div class="checkout-container">
  <div *ngIf="loading" class="loading-overlay">
    <div class="spinner"></div>
    <p>{{ processingMessage || 'Processing...' }}</p>
  </div>
  
  <div *ngIf="error" class="error-message">
    {{ error }}
    <button (click)="error = ''">Dismiss</button>
  </div>
  
  <div class="order-summary" *ngIf="orderDetails">
    <h2>Order Summary</h2>
    <div class="order-items">
      <div *ngFor="let item of orderDetails.items" class="order-item">
        <div class="item-details">
          <img [src]="item.productImage" alt="{{ item.productName }}">
          <div>
            <h3>{{ item.productName }}</h3>
            <p>Quantity: {{ item.quantity }}</p>
            <p>Price: {{ item.price | currency }}</p>
          </div>
        </div>
      </div>
    </div>
    
    <div class="order-totals">
      <p>Total: <strong>{{ orderDetails.totalAmount | currency }}</strong></p>
    </div>
    
    <button 
      (click)="processPayment(orderDetails.id)" 
      [disabled]="loading" 
      class="pay-button">
      Pay Now
    </button>
  </div>
</div>
-->


// ============================================================================
// Example Module setup (app.module.ts)
// ============================================================================

/*
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CheckoutComponent } from './components/checkout/checkout.component';

@NgModule({
  declarations: [
    AppComponent,
    CheckoutComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
*/


// ============================================================================
// Make sure to include scripts in your index.html:
// ============================================================================

/*
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>E-commerce App</title>
  <base href="/">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="icon" type="image/x-icon" href="favicon.ico">
  
  <!-- Razorpay SDK -->
  <script src="https://checkout.razorpay.com/v1/checkout.js"></script>
  
  <!-- Auto-verification helper -->
  <script src="/js/razorpay-auto-verify.js"></script>
</head>
<body>
  <app-root></app-root>
</body>
</html>
*/ 