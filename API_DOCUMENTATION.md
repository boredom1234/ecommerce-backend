# E-commerce API Documentation

## Table of Contents
1. [Authentication](#1-authentication)
2. [User APIs](#2-user-apis)
3. [Product APIs](#3-product-apis)
4. [Cart APIs](#4-cart-apis)
5. [Order APIs](#5-order-apis)
6. [Admin APIs](#6-admin-apis)
7. [Staff Permissions](#7-staff-permissions)
8. [Recent Improvements](#8-recent-improvements)
9. [Additional APIs](#9-additional-apis)
10. [Admin API Logging](#10-admin-api-logging)

## 1. Authentication

### 1.1 User Registration

#### 1.1.1 Direct Registration
Registers a new user with complete details including name, address, city, state, postal code, country, and phone number.

**Request:**
```bash
curl -X POST "http://localhost:5000/api/register" \
-H "Content-Type: application/json" \
-d "{\"email\": \"user@example.com\", \"password\": \"userpassword\", \"name\": \"John Doe\", \"address\": \"123 Main St\", \"city\": \"New York\", \"state\": \"NY\", \"postalCode\": \"10001\", \"country\": \"USA\", \"phoneNumber\": \"+1234567890\"}"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Registration successful",
    "data": {
        "id": 1,
        "email": "user@example.com",
        "name": "John Doe",
        "address": "123 Main St",
        "city": "New York",
        "state": "NY",
        "postalCode": "10001",
        "country": "USA",
        "phoneNumber": "+1234567890",
        "role": "USER"
    }
}
```

### 1.2 User Registration (Two-Step Process)

#### 1.2.1 Request OTP
Sends a one-time password (OTP) to the specified email address for verification.

**Request:**
```bash
curl -X POST "http://localhost:5000/api/register/send-otp?email=user@example.com"
```

**Example Response:**
```json
{
    "success": true,
    "message": "OTP sent successfully to user@example.com"
}
```

#### 1.2.2 Verify OTP and Complete Registration
Verifies the OTP and creates a new user account with optional additional details.

**Request:**
```bash
curl -X POST "http://localhost:5000/api/register/verify-otp?password=userpassword&name=John%20Doe&address=123%20Main%20St&city=New%20York&state=NY&postalCode=10001&country=USA&phoneNumber=%2B1234567890" \
-H "Content-Type: application/json" \
-d "{\"email\": \"user@example.com\", \"otp\": \"123456\"}"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Registration successful",
    "data": {
        "id": 1,
        "email": "user@example.com",
        "name": "John Doe",
        "address": "123 Main St",
        "city": "New York",
        "state": "NY",
        "postalCode": "10001",
        "country": "USA",
        "phoneNumber": "+1234567890",
        "role": "USER"
    }
}
```

### 1.3 User Login
Authenticates a user and returns a JWT token.

**Request:**
```bash
curl -X POST "http://localhost:5000/api/login" \
-H "Content-Type: application/json" \
-d "{\"email\": \"user@example.com\", \"password\": \"userpassword\"}"
```

**Example Response:**
```json
{
    "success": true,
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
        "id": 1,
        "email": "user@example.com",
        "name": "John Doe",
        "address": "123 Main St",
        "city": "New York",
        "state": "NY",
        "postalCode": "10001",
        "country": "USA",
        "phoneNumber": "+1234567890",
        "role": "USER"
    }
}
```

### 1.4 Admin Registration
Creates a new admin account using a secure admin key.

**Request:**
```bash
curl -X POST "http://localhost:5000/api/admin/auth/register?adminKey=admin-secret-key-12345" \
-H "Content-Type: application/json" \
-d "{\"email\": \"admin@example.com\", \"password\": \"adminpassword\"}"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Admin registered successfully",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
        "id": 2,
        "email": "admin@example.com",
        "role": "ADMIN"
    }
}
```

### 1.5 Admin Login
Authenticates an admin user.

**Request:**
```bash
curl -X POST "http://localhost:5000/api/admin/auth/login" \
-H "Content-Type: application/json" \
-d "{\"email\": \"admin@example.com\", \"password\": \"adminpassword\"}"
```

**Example Response:**
```json
{
    "success": true,
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
        "id": 2,
        "email": "admin@example.com",
        "role": "ADMIN"
    }
}
```

### 1.6 Staff Authentication

Staff members do not register themselves. They are created by administrators and then log in using their provided credentials.

#### 1.6.1 Staff Login
Authenticates a staff member.

**Request:**
```bash
curl -X POST "http://localhost:5000/api/login" \
-H "Content-Type: application/json" \
-d "{\"email\": \"staff@example.com\", \"password\": \"staffpassword\"}"
```

**Example Response:**
```json
{
    "success": true,
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
        "id": 3,
        "email": "staff@example.com",
        "name": "Staff Member",
        "address": "456 Staff St",
        "city": "San Francisco",
        "state": "CA",
        "postalCode": "94105",
        "country": "USA",
        "phoneNumber": "+1234567891",
        "role": "STAFF"
    }
}
```

### 1.7 Password Reset Request
Initiates the password reset process by sending a reset token to the user's email.

**Request:**
```bash
curl -X POST "http://localhost:5000/api/forgot-password?email=user@example.com"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Password reset instructions sent to your email"
}
```

### 1.8 Password Reset
Resets the user's password using the token received via email.

**Request:**
```bash
curl -X POST "http://localhost:5000/api/reset-password?token=RESET_TOKEN&newPassword=newpassword"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Password reset successful"
}
```

### 1.9 Google OAuth Authentication

Authenticate users via Google OAuth. This allows users to sign up or sign in using their Google account.

#### 1.9.1 Google Login/Sign Up

Authenticates a user using a Google ID token. If the email doesn't exist in the system, a new account will be created with the required user information.

**Request:**
```bash
curl -X POST "http://localhost:5000/api/auth/google/login" \
-H "Content-Type: application/json" \
-d "{\"tokenId\": \"GOOGLE_ID_TOKEN\"}"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Google authentication successful",
    "data": {
        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        "user": {
            "id": 4,
            "email": "user@gmail.com",
            "name": "Google User",
            "address": null,
            "city": null,
            "state": null,
            "postalCode": null,
            "country": null,
            "phoneNumber": null,
            "role": "USER"
        }
    }
}
```

**Notes:**
- For new users, the system will create an account with the email and name from their Google profile
- If the email already exists but wasn't previously linked to Google, the account will be updated to use Google authentication
- All users authenticated via Google will have their 'verified' status set to true automatically
- For new users signing up with Google, additional profile information (address, phone) can be updated later
- A JWT token is generated, which can be used for accessing authenticated endpoints

## 2. User APIs

### 2.1 Get User Details
Retrieves the authenticated user's profile information.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/user-details" \
-H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Example Response:**
```json
{
    "id": 1,
    "email": "user@example.com",
    "name": "John Doe",
    "address": "123 Main St",
    "city": "New York",
    "state": "NY",
    "postalCode": "10001",
    "country": "USA",
    "phoneNumber": "+1234567890",
    "role": "USER",
    "createdAt": "2024-03-15T10:30:00Z",
    "orders": [...],
    "cart": {...}
}
```

### 2.2 Update User Profile
Updates the authenticated user's profile information including name, address, city, state, postal code, country, phone number, and optionally changes the password.

**Request:**
```bash
curl -X PUT "http://localhost:5000/api/update-profile" \
-H "Content-Type: application/json" \
-H "Authorization: Bearer YOUR_JWT_TOKEN" \
-d "{\"name\": \"Updated Name\", \"address\": \"New Address\", \"city\": \"San Francisco\", \"state\": \"CA\", \"postalCode\": \"94105\", \"country\": \"USA\", \"phoneNumber\": \"+0987654321\", \"currentPassword\": \"currentpassword\", \"newPassword\": \"newpassword\"}"
```

**Notes:**
- All fields are optional. Only include the fields you want to update.
- To change the password, both `currentPassword` and `newPassword` must be provided.
- The current password must be correct for the password update to succeed.

**Example Response:**
```json
{
    "success": true,
    "message": "Profile updated successfully",
    "data": {
        "id": 1,
        "email": "user@example.com",
        "name": "Updated Name",
        "address": "New Address",
        "city": "San Francisco",
        "state": "CA",
        "postalCode": "94105",
        "country": "USA",
        "phoneNumber": "+0987654321",
        "role": "USER"
    }
}
```

## 3. Product APIs

### 3.1 Get All Products (Public)
Retrieves a paginated list of active products.

**Request:**
```bash
# Get first page (10 products per page)
curl -X GET "http://localhost:5000/api/products?page=0&size=10"
```

**Example Response:**
```json
{
    "content": [
        {
            "id": 1,
            "name": "Smartphone X",
            "description": "Latest smartphone model",
            "price": 999.99,
            "category": "Electronics",
            "imageUrl": "http://example.com/smartphone.jpg",
            "averageRating": 4.5
        }
    ],
    "totalPages": 5,
    "totalElements": 50,
    "currentPage": 0
}
```

### 3.2 Get Product Details
Retrieves detailed information about a specific product.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/product/1"
```

**Example Response:**
```json
{
    "id": 1,
    "name": "Smartphone X",
    "description": "Latest smartphone model",
    "price": 999.99,
    "category": "Electronics",
    "imageUrl": "http://example.com/smartphone.jpg",
    "stock": 25,
    "averageRating": 4.5,
    "reviews": [
        {
            "id": 1,
            "rating": 5,
            "comment": "Great product!",
            "userId": 1,
            "createdAt": "2024-03-15T10:30:00Z"
        }
    ]
}
```

### 3.3 Search Products
Searches products based on keywords.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/product-search?query=smartphone"
```

**Example Response:**
```json
{
    "results": [
        {
            "id": 1,
            "name": "Smartphone X",
            "description": "Latest smartphone model",
            "price": 999.99,
            "category": "Electronics"
        }
    ],
    "totalResults": 1
}
```

### 3.4 Get Products by Category
Retrieves products filtered by category.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/products/category/Electronics"
```

**Example Response:**
```json
{
    "content": [
        {
            "id": 1,
            "name": "Smartphone X",
            "price": 999.99,
            "category": "Electronics"
        }
    ],
    "totalProducts": 10
}
```

### 3.5 Product Reviews

#### 3.5.1 Add Review
Adds a review for a specific product.

**Request:**
```bash
curl -X POST "http://localhost:5000/api/product/1/review?rating=5&comment=Great%20product!" \
-H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "review": {
        "id": 1,
        "productId": 1,
        "userId": 1,
        "rating": 5,
        "comment": "Great product!",
        "createdAt": "2024-03-15T10:30:00Z"
    }
}
```

#### 3.5.2 Get Product Reviews
Retrieves all reviews for a specific product.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/product/1/reviews"
```

**Example Response:**
```json
{
    "reviews": [
        {
            "id": 1,
            "rating": 5,
            "comment": "Great product!",
            "userId": 1,
            "createdAt": "2024-03-15T10:30:00Z"
        }
    ],
    "averageRating": 5.0,
    "totalReviews": 1
}
```

## 4. Cart APIs

### 4.1 Get Cart Items
Retrieves the current user's shopping cart.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/cart" \
-H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Example Response:**
```json
{
    "items": [
        {
            "id": 1,
            "productId": 1,
            "name": "Smartphone X",
            "quantity": 2,
            "price": 999.99,
            "totalPrice": 1999.98
        }
    ],
    "totalItems": 2,
    "subtotal": 1999.98
}
```

### 4.2 Add to Cart
Adds a product to the shopping cart.

**Request:**
```bash
curl -X POST "http://localhost:5000/api/cart-add/1?quantity=1" \
-H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Product added to cart",
    "cart": {
        "items": [...],
        "totalItems": 3,
        "subtotal": 2999.97
    }
}
```

### 4.3 Update Cart Item
Updates the quantity of a cart item.

**Request:**
```bash
curl -X PATCH "http://localhost:5000/api/cart-update/1?quantity=3" \
-H "Authorization: Bearer YOUR_JWT_TOKEN"
```
> Note: The ID in the URL is the cart item ID, not the product ID.

**Example Response:**
```json
{
    "success": true,
    "message": "Cart item updated successfully",
    "data": {
        "id": 1,
        "productId": 1,
        "productName": "Smartphone X",
        "productImage": "http://example.com/smartphone.jpg",
        "quantity": 3,
        "price": 999.99,
        "totalPrice": 2999.97
    }
}
```

### 4.4 Remove from Cart
Removes an item from the cart.

**Request:**
```bash
curl -X DELETE "http://localhost:5000/api/cart-delete/1" \
-H "Authorization: Bearer YOUR_JWT_TOKEN"
```
> Note: The ID in the URL is the cart item ID, not the product ID.

**Example Response:**
```json
{
    "success": true,
    "message": "Item removed from cart successfully",
    "data": null
}
```

## 5. Order APIs

### 5.1 Place Order
Creates a new order from the current cart.

**Request:**
```bash
curl -X POST "http://localhost:5000/api/order-place?shippingAddress=123%20Main%20St%2C%20City" \
-H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "order": {
        "id": 1,
        "items": [...],
        "totalAmount": 1999.98,
        "shippingAddress": "123 Main St, City",
        "status": "PENDING",
        "createdAt": "2024-03-15T10:30:00Z"
    }
}
```

### 5.2 Get Orders
Retrieves all orders for the current user.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/orders" \
-H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Example Response:**
```json
{
    "orders": [
        {
            "id": 1,
            "totalAmount": 1999.98,
            "status": "PENDING",
            "createdAt": "2024-03-15T10:30:00Z"
        }
    ],
    "totalOrders": 1
}
```

### 5.3 Get Order Details
Retrieves detailed information about a specific order.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/order/1" \
-H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Example Response:**
```json
{
    "id": 1,
    "items": [
        {
            "productId": 1,
            "name": "Smartphone X",
            "quantity": 2,
            "price": 999.99
        }
    ],
    "totalAmount": 1999.98,
    "shippingAddress": "123 Main St, City",
    "status": "PENDING",
    "createdAt": "2024-03-15T10:30:00Z"
}
```

### 5.4 Cancel Order
Allows a customer to cancel their order. Orders can only be cancelled if they haven't been delivered or already cancelled.

**Request:**
```bash
curl -X DELETE "http://localhost:5000/api/order/{id}/cancel" \
-H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Order cancelled successfully",
    "data": null
}
```

> Note: When an order is cancelled, the product stock quantities are automatically restored.

### 5.4.1 Check if Order Can Be Cancelled
Checks if an order is eligible for cancellation based on its current status.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/order/{id}/can-cancel" \
-H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Order cancellation status retrieved",
    "data": {
        "canCancel": true
    }
}
```

### 5.5 Stock Management in Orders

When an order is placed, the system automatically updates the product stock levels. If a product's stock level would go below 0 after placing an order, the order will fail with an "insufficient stock" error.

#### 5.5.1 Stock Reservation
During the checkout process, stock can be temporarily reserved for a short period.

**Request:**
```bash
curl -X POST "http://localhost:5000/api/order/reserve-stock" \
-H "Authorization: Bearer YOUR_JWT_TOKEN" \
-H "Content-Type: application/json" \
-d "{\"items\": [{\"productId\": 1, \"quantity\": 2}, {\"productId\": 3, \"quantity\": 1}]}"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Stock reserved successfully",
    "data": {
        "reservationId": "res_12345",
        "expiresAt": "2024-03-15T11:00:00Z",
        "items": [
            {
                "productId": 1,
                "name": "Smartphone X",
                "quantity": 2,
                "available": true
            },
            {
                "productId": 3,
                "name": "Cotton Polo Shirt",
                "quantity": 1,
                "available": true
            }
        ]
    }
}
```

#### 5.5.2 Stock Availability Check
Checks if products are in stock before attempting to place an order.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/products/check-stock?productId=1&quantity=5&productId=2&quantity=3" \
-H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "availabilityStatus": [
        {
            "productId": 1,
            "name": "Smartphone X",
            "requested": 5,
            "available": 25,
            "inStock": true
        },
        {
            "productId": 2,
            "name": "Laptop Pro",
            "requested": 3,
            "available": 2,
            "inStock": false
        }
    ],
    "allInStock": false
}
```

## 6. Admin APIs

### 6.1 Product Management

#### 6.1.1 Get All Products (Admin View)
Retrieves all products with additional administrative information.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/admin/products?page=0&size=10" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
    "content": [
        {
            "id": 1,
            "name": "Smartphone X",
            "price": 999.99,
            "stock": 50,
            "active": true,
            "createdAt": "2024-03-15T10:30:00Z",
            "lastModified": "2024-03-15T10:30:00Z"
        }
    ],
    "totalPages": 5,
    "totalElements": 50
}
```

#### 6.1.2 Add Product
Creates a new product.

**Request:**
```bash
curl -X POST "http://localhost:5000/api/admin/product" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
-H "Content-Type: application/json" \
-d "{\"name\": \"New Product\", \"description\": \"Product Description\", \"price\": 99.99, \"imageUrl\": \"http://example.com/image.jpg\", \"category\": \"Electronics\", \"stock\": 50, \"active\": true}"
```

**Example Response:**
```json
{
    "success": true,
    "product": {
        "id": 2,
        "name": "New Product",
        "description": "Product Description",
        "price": 99.99,
        "category": "Electronics",
        "stock": 50,
        "active": true
    }
}
```

#### 6.1.3 Update Product
Updates an existing product.

**Request:**
```bash
curl -X PATCH "http://localhost:5000/api/admin/product/1" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
-H "Content-Type: application/json" \
-d "{\"name\": \"Updated Name\", \"price\": 89.99, \"stock\": 45}"
```

**Example Response:**
```json
{
    "success": true,
    "product": {
        "id": 1,
        "name": "Updated Name",
        "price": 89.99,
        "stock": 45,
        "lastModified": "2024-03-15T11:30:00Z"
    }
}
```

#### 6.1.4 Delete Product
Deletes an existing product. Admin only operation.

**Request:**
```bash
curl -X DELETE "http://localhost:5000/api/admin/product/1" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Product deleted successfully",
    "data": null
}
```

### 6.2 Order Management

#### 6.2.1 Get All Orders
Retrieves all orders in the system.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/admin/orders" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
    "orders": [
        {
            "id": 1,
            "userId": 1,
            "totalAmount": 1999.98,
            "status": "PENDING",
            "createdAt": "2024-03-15T10:30:00Z"
        }
    ],
    "totalOrders": 1
}
```

#### 6.2.2 Update Order Status
Updates the status of an order.

**Request:**
```bash
curl -X PATCH "http://localhost:5000/api/admin/order/1/status?status=SHIPPED" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "order": {
        "id": 1,
        "status": "SHIPPED",
        "lastModified": "2024-03-15T11:30:00Z"
    }
}
```

#### 6.2.3 Get Order Details
Retrieves detailed information about a specific order including items, customer details, and status history.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/admin/order/1" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "order": {
        "id": 1,
        "userId": 1,
        "userEmail": "user@example.com",
        "userName": "John Doe",
        "totalAmount": 1999.98,
        "shippingAddress": "123 Main St, City",
        "phoneNumber": "+1234567890",
        "status": "SHIPPED",
        "paymentMethod": "CREDIT_CARD",
        "paymentStatus": "PAID",
        "createdAt": "2024-03-15T10:30:00Z",
        "lastModified": "2024-03-15T11:30:00Z",
        "items": [
            {
                "productId": 1,
                "name": "Smartphone X",
                "quantity": 1,
                "price": 999.99,
                "subtotal": 999.99
            },
            {
                "productId": 2,
                "name": "Laptop Pro",
                "quantity": 1,
                "price": 999.99,
                "subtotal": 999.99
            }
        ],
        "statusHistory": [
            {
                "status": "PENDING",
                "timestamp": "2024-03-15T10:30:00Z"
            },
            {
                "status": "PROCESSING",
                "timestamp": "2024-03-15T11:00:00Z"
            },
            {
                "status": "SHIPPED",
                "timestamp": "2024-03-15T11:30:00Z"
            }
        ]
    }
}
```

### 6.3 User Management

#### 6.3.1 Get All Users
Retrieves all users in the system.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/admin/users" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
    "users": [
        {
            "id": 1,
            "email": "user@example.com",
            "role": "USER",
            "createdAt": "2024-03-15T10:30:00Z"
        }
    ],
    "totalUsers": 1
}
```

#### 6.3.2 Update User Role
Updates a user's role.

**Request:**
```bash
curl -X PATCH "http://localhost:5000/api/admin/user/1/role?role=ADMIN" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "user": {
        "id": 1,
        "email": "user@example.com",
        "role": "ADMIN",
        "lastModified": "2024-03-15T11:30:00Z"
    }
}
```

#### 6.3.3 Advanced User Profile Management

##### 6.3.3.1 Search Users
Searches for users based on email, name, or role.

**Request:**
```bash
# Search by email
curl -X GET "http://localhost:5000/api/admin/users/search?email=user" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"

# Search by name
curl -X GET "http://localhost:5000/api/admin/users/search?name=John" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"

# Filter by role with pagination
curl -X GET "http://localhost:5000/api/admin/users/search?role=USER&page=0&size=20" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Users retrieved successfully",
    "data": [
        {
            "id": 1,
            "email": "user@example.com",
            "name": "John Doe",
            "address": "123 Main St",
            "city": "New York",
            "state": "NY",
            "postalCode": "10001",
            "country": "USA",
            "phoneNumber": "+1234567890",
            "role": "USER"
        }
    ]
}
```

##### 6.3.3.2 Get User Profile
Retrieves detailed profile information for a specific user.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/admin/user-profile/1" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "message": "User profile retrieved successfully",
    "data": {
        "id": 1,
        "email": "user@example.com",
        "name": "John Doe",
        "address": "123 Main St",
        "city": "New York",
        "state": "NY",
        "postalCode": "10001",
        "country": "USA",
        "phoneNumber": "+1234567890",
        "role": "USER",
        "verified": true,
        "createdAt": "2024-03-15T10:30:00Z",
        "lastLogin": "2024-03-17T15:45:00Z",
        "orderCount": 5,
        "totalSpent": 1299.95,
        "lastOrderDate": "2024-03-16T08:20:00Z",
        "accountLocked": false,
        "accountStatus": "ACTIVE"
    }
}
```

##### 6.3.3.3 Get All User Profiles
Retrieves detailed profile information for all users.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/admin/users/profiles" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "message": "User profiles retrieved successfully",
    "data": [
        {
            "id": 1,
            "email": "user@example.com",
            "name": "John Doe",
            "address": "123 Main St",
            "city": "New York",
            "state": "NY",
            "postalCode": "10001",
            "country": "USA",
            "phoneNumber": "+1234567890",
            "role": "USER",
            "verified": true,
            "createdAt": "2024-03-15T10:30:00Z",
            "lastLogin": "2024-03-17T15:45:00Z",
            "accountStatus": "ACTIVE"
        },
        {
            "id": 3,
            "email": "jane@example.com",
            "name": "Jane Smith",
            "address": "456 Oak Ave",
            "city": "Los Angeles",
            "state": "CA",
            "postalCode": "90001",
            "country": "USA",
            "phoneNumber": "+9876543210",
            "role": "USER",
            "verified": false,
            "createdAt": "2024-03-18T09:15:00Z",
            "lastLogin": null,
            "accountStatus": "UNVERIFIED"
        }
    ]
}
```

##### 6.3.3.4 Update User Profile
Updates a user's profile information and account status.

**Request:**
```bash
curl -X PATCH "http://localhost:5000/api/admin/user-profile/1" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
-H "Content-Type: application/json" \
-d "{\"name\": \"Updated Name\", \"address\": \"New Address\", \"city\": \"Boston\", \"state\": \"MA\", \"postalCode\": \"02108\", \"country\": \"USA\", \"phoneNumber\": \"+9876543210\", \"accountStatus\": \"ACTIVE\"}"
```

**Example Response:**
```json
{
    "success": true,
    "message": "User profile updated successfully",
    "data": {
        "id": 1,
        "email": "user@example.com",
        "name": "Updated Name",
        "address": "New Address",
        "city": "Boston",
        "state": "MA",
        "postalCode": "02108",
        "country": "USA",
        "phoneNumber": "+9876543210",
        "role": "USER",
        "accountStatus": "ACTIVE"
    }
}
```

##### 6.3.3.5 Toggle User Account Lock
Locks or unlocks a user account.

**Request:**
```bash
curl -X PATCH "http://localhost:5000/api/admin/user/1/toggle-lock" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "message": "User account lock status toggled successfully",
    "data": null
}
```

##### 6.3.3.6 Reset User Password
Resets a user's password.

**Request:**
```bash
curl -X POST "http://localhost:5000/api/admin/user/1/reset-password?newPassword=newpassword123" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "message": "User password reset successfully",
    "data": null
}
```

##### 6.3.3.7 Get User Statistics
Retrieves statistics about users in the system.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/admin/users/statistics" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Users statistics retrieved successfully",
    "data": {
        "totalUsers": 25,
        "adminCount": 2,
        "staffCount": 3,
        "customerCount": 20,
        "lockedAccounts": 1,
        "unverifiedAccounts": 3
    }
}
```

### 6.4 Admin Profile Management

#### 6.4.1 Get Admin Profile
Retrieves detailed profile information for a specific admin.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/admin/admin-profile/2" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Admin profile retrieved successfully",
    "data": {
        "id": 2,
        "email": "admin@example.com",
        "name": "Admin User",
        "address": "456 Admin St",
        "city": "Chicago",
        "state": "IL",
        "postalCode": "60601",
        "country": "USA",
        "phoneNumber": "+1234567899",
        "role": "ADMIN",
        "verified": true,
        "lastLogin": "2024-03-17T14:30:00Z",
        "createdAt": "2024-03-01T09:00:00Z",
        "managedUsersCount": 23,
        "createdProductsCount": 150,
        "processedOrdersCount": 87
    }
}
```

#### 6.4.2 Get All Admin Profiles
Retrieves detailed profile information for all admins.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/admin/admin-profiles" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Admin profiles retrieved successfully",
    "data": [
        {
            "id": 2,
            "email": "admin@example.com",
            "name": "Admin User",
            "address": "456 Admin St",
            "city": "Chicago",
            "state": "IL",
            "postalCode": "60601",
            "country": "USA",
            "phoneNumber": "+1234567899",
            "role": "ADMIN",
            "lastLogin": "2024-03-17T14:30:00Z",
            "createdAt": "2024-03-01T09:00:00Z",
            "managedUsersCount": 23
        },
        {
            "id": 5,
            "email": "admin2@example.com",
            "name": "Second Admin",
            "address": "789 Admin Ave",
            "city": "Boston",
            "state": "MA",
            "postalCode": "02108",
            "country": "USA",
            "phoneNumber": "+8765432109",
            "role": "ADMIN",
            "lastLogin": "2024-03-16T11:20:00Z",
            "createdAt": "2024-03-10T10:15:00Z",
            "managedUsersCount": 0
        }
    ]
}
```

#### 6.4.3 Get Current Admin Profile
Retrieves the profile information for the currently authenticated admin.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/admin/current-admin-profile" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Current admin profile retrieved successfully",
    "data": {
        "id": 2,
        "email": "admin@example.com",
        "name": "Admin User",
        "address": "456 Admin St",
        "city": "Chicago",
        "state": "IL",
        "postalCode": "60601",
        "country": "USA",
        "phoneNumber": "+1234567899",
        "role": "ADMIN",
        "verified": true,
        "lastLogin": "2024-03-17T14:30:00Z",
        "createdAt": "2024-03-01T09:00:00Z",
        "managedUsersCount": 23,
        "createdProductsCount": 150,
        "processedOrdersCount": 87
    }
}
```

#### 6.4.4 Update Admin Profile
Updates an admin's profile information.

**Request:**
```bash
curl -X PATCH "http://localhost:5000/api/admin/admin-profile/2" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
-H "Content-Type: application/json" \
-d "{\"name\": \"Updated Admin Name\", \"address\": \"New Admin Address\", \"city\": \"Seattle\", \"state\": \"WA\", \"postalCode\": \"98101\", \"country\": \"USA\", \"phoneNumber\": \"+9876543211\"}"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Admin profile updated successfully",
    "data": {
        "id": 2,
        "email": "admin@example.com",
        "name": "Updated Admin Name",
        "address": "New Admin Address",
        "city": "Seattle",
        "state": "WA",
        "postalCode": "98101",
        "country": "USA",
        "phoneNumber": "+9876543211",
        "role": "ADMIN"
    }
}
```

### 6.5 Staff Management

#### 6.5.1 Register Staff Member
Creates a new staff member account with specific permissions. Only administrators can create staff accounts.

**Request:**
```bash
curl -X POST "http://localhost:5000/api/admin/staff/register" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
-H "Content-Type: application/json" \
-d "{\"email\": \"staff@example.com\", \"password\": \"staffpassword\", \"name\": \"Staff Member\", \"address\": \"456 Staff St\", \"city\": \"New York\", \"state\": \"NY\", \"postalCode\": \"10001\", \"country\": \"USA\", \"phoneNumber\": \"+1234567891\", \"permissions\": [\"VIEW_ORDERS\", \"UPDATE_ORDER_STATUS\", \"VIEW_PRODUCTS\"]}"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Staff registered successfully",
    "data": {
        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        "user": {
            "id": 3,
            "email": "staff@example.com",
            "name": "Staff Member",
            "address": "456 Staff St",
            "city": "New York",
            "state": "NY",
            "postalCode": "10001",
            "country": "USA",
            "phoneNumber": "+1234567891",
            "role": "STAFF"
        }
    }
}
```

#### 6.5.2 Get All Staff Members
Retrieves a list of all staff members. Admin only.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/admin/staff" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Staff retrieved successfully",
    "data": [
        {
            "id": 1,
            "userId": 3,
            "email": "staff@example.com",
            "name": "Staff Member",
            "permissions": ["VIEW_ORDERS", "UPDATE_ORDER_STATUS", "VIEW_PRODUCTS"],
            "createdBy": 2
        }
    ]
}
```

#### 6.5.3 Get Staff Member Details
Retrieves detailed information about a specific staff member. Admin only.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/admin/staff/1" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Staff retrieved successfully",
    "data": {
        "id": 1,
        "userId": 3,
        "email": "staff@example.com",
        "name": "Staff Member",
        "permissions": ["VIEW_ORDERS", "UPDATE_ORDER_STATUS", "VIEW_PRODUCTS"],
        "createdBy": 2
    }
}
```

#### 6.5.4 Update Staff Permissions
Updates the permissions of a staff member. Admin only.

**Request:**
```bash
curl -X PATCH "http://localhost:5000/api/admin/staff/1/permissions" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
-H "Content-Type: application/json" \
-d "[\"VIEW_ORDERS\", \"VIEW_PRODUCTS\", \"ADD_PRODUCTS\"]"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Staff permissions updated successfully",
    "data": {
        "id": 1,
        "userId": 3,
        "email": "staff@example.com",
        "name": "Staff Member",
        "permissions": ["VIEW_ORDERS", "VIEW_PRODUCTS", "ADD_PRODUCTS"],
        "createdBy": 2
    }
}
```

#### 6.5.5 Delete Staff Member
Removes a staff member. Admin only.

**Request:**
```bash
curl -X DELETE "http://localhost:5000/api/admin/staff/1" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Staff deleted successfully",
    "data": null
}
```

### 6.6 Stock Management

#### 6.6.1 Update Product Stock
Updates the stock of a specific product.

**Request:**
```bash
curl -X PATCH "http://localhost:5000/api/admin/product/1/stock?quantity=50" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Stock updated successfully",
    "data": {
        "id": 1,
        "name": "Smartphone X",
        "stock": 50,
        "lastModified": "2024-03-15T11:30:00Z"
    }
}
```

#### 6.6.2 Get Low Stock Products
Retrieves products with stock below a specified threshold.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/admin/products/low-stock?threshold=10" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "products": [
        {
            "id": 15,
            "name": "Oversized Coat",
            "category": "Women",
            "stock": 8,
            "price": 149.00
        },
        {
            "id": 6,
            "name": "Leather Jacket",
            "category": "Men",
            "stock": 5,
            "price": 199.99
        }
    ],
    "totalLowStock": 2
}
```

#### 6.6.3 Bulk Update Stock
Updates the stock of multiple products at once.

**Request:**
```bash
curl -X POST "http://localhost:5000/api/admin/products/bulk-stock-update" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
-H "Content-Type: application/json" \
-d "[{\"productId\": 1, \"stock\": 100}, {\"productId\": 2, \"stock\": 75}]"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Bulk stock update successful",
    "updatedProducts": [
        {
            "id": 1,
            "name": "Smartphone X",
            "stock": 100
        },
        {
            "id": 2,
            "name": "Laptop Pro",
            "stock": 75
        }
    ]
}
```

### 6.7 Admin Dashboard Statistics

Provides comprehensive statistics and analytics for the admin dashboard.

#### 6.7.1 Get Dashboard Statistics
Retrieves detailed statistics about orders, revenue, products, and customers.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/admin/dashboard/statistics" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Dashboard statistics retrieved successfully",
    "data": {
        "financialStatistics": {
            "totalRevenue": 25890.50,
            "monthlyRevenue": 4350.75,
            "weeklyRevenue": 1250.25,
            "dailyRevenue": 450.00,
            "averageOrderValue": 85.50
        },
        "orderStatistics": {
            "totalOrders": 302,
            "pendingOrders": 15,
            "confirmedOrders": 28,
            "shippedOrders": 42,
            "deliveredOrders": 210,
            "cancelledOrders": 7,
            "conversionRate": 3.5
        },
        "productStatistics": {
            "totalProducts": 150,
            "lowStockProducts": 12,
            "outOfStockProducts": 5,
            "topSellingProducts": [
                {
                    "id": 25,
                    "name": "Premium Headphones",
                    "imageUrl": "http://example.com/headphones.jpg",
                    "totalQuantity": 85,
                    "totalRevenue": 8415.15
                },
                {
                    "id": 42,
                    "name": "Wireless Charger",
                    "imageUrl": "http://example.com/charger.jpg",
                    "totalQuantity": 67,
                    "totalRevenue": 2010.00
                }
            ]
        },
        "customerStatistics": {
            "totalCustomers": 250,
            "newCustomersThisMonth": 28,
            "activeCustomers": 180,
            "topCustomers": [
                {
                    "id": 15,
                    "name": "John Smith",
                    "email": "john@example.com",
                    "totalSpent": 1250.50,
                    "orderCount": 12
                },
                {
                    "id": 23,
                    "name": "Sarah Johnson",
                    "email": "sarah@example.com",
                    "totalSpent": 975.25,
                    "orderCount": 8
                }
            ]
        },
        "userStatistics": {
            "totalUsersCount": 275,
            "usersByRole": {
                "ADMIN": 5,
                "STAFF": 20,
                "USER": 250
            }
        },
        "timeBasedStatistics": {
            "revenueByMonth": {
                "2024-01": 3250.75,
                "2024-02": 3890.50,
                "2024-03": 4350.75
            },
            "revenueByDay": {
                "2024-03-20": 345.50,
                "2024-03-21": 450.00
            },
            "ordersByMonth": {
                "2024-01": 85,
                "2024-02": 95,
                "2024-03": 102
            },
            "ordersByDay": {
                "2024-03-20": 8,
                "2024-03-21": 10
            }
        }
    }
}
```

**Notes:**
- This endpoint is restricted to admins only
- The statistics include financial, order, product, customer, and time-based analytics
- Revenue values are in the store's default currency
- Top selling products and top customers are limited to 10 results
- Time-based statistics include data for the past year (monthly) and past 30 days (daily)
- Conversion rate represents the percentage of users who have placed at least one order

## 7. Staff Permissions

Staff members have limited access compared to administrators. Their permissions can be customized when creating or updating their accounts. Available permissions include:

- **VIEW_ORDERS**: Allows viewing order details and listings
- **UPDATE_ORDER_STATUS**: Allows changing order status (e.g., pending to shipped)
- **VIEW_PRODUCTS**: Allows viewing detailed product information
- **UPDATE_PRODUCTS**: Allows editing existing product details
- **ADD_PRODUCTS**: Allows adding new products
- **VIEW_CUSTOMERS**: Allows viewing customer information
- **MANAGE_STOCK**: Allows updating product stock levels
- **VIEW_STOCK_REPORTS**: Allows viewing stock level reports and low stock alerts

Staff members cannot:
- Create, modify or delete other staff members or admin accounts
- Access user role management functions
- Delete products (this is admin-only)
- Completely deplete stock without admin approval (stock cannot be set to less than 0)

## 8. Recent Improvements

### 8.1 Admin Profile Management

The API has been enhanced with comprehensive admin profile management capabilities that enable administrators to efficiently monitor and manage all users and administrators in the system.

#### 8.1.1 Key Features

##### Enhanced User Profiles
- **Detailed User Information**: View comprehensive user profiles with order counts, total spending, and last order dates
- **Account Status Management**: Lock/unlock user accounts and track verification status
- **Advanced Search**: Search and filter users by email, name, or role
- **User Statistics**: Access aggregated user statistics by role and account status

##### Admin Profiles
- **Activity Metrics**: Monitor admin effectiveness with statistics on managed users, created products, and processed orders
- **Admin Dashboard**: Quick access to key performance indicators for administrators
- **Self-Management**: Administrators can view and update their own profiles

#### 8.1.2 Technical Improvements

- Added database tracking for user account status (locked/unlocked)
- Implemented last login tracking for security monitoring
- Added account creation timestamp for user lifecycle management
- Optimized database queries for efficient user searching and filtering

### 8.2 User Experience Enhancements

The admin interface now provides more intuitive management capabilities:

- **Bulk Actions**: Efficiently manage multiple users with batch operations
- **Role Management**: Easily promote users to staff or admin roles as needed
- **Password Management**: Reset user passwords without needing to delete accounts
- **Activity Tracking**: Monitor user login patterns and detect unusual activity

For detailed documentation on these features, refer to sections 6.3.3 (Advanced User Profile Management) and 6.4 (Admin Profile Management).

## 9. Additional APIs

### 9.1 User Authentication Additional Endpoints

#### 9.1.1 User Logout
Logs out the currently authenticated user.

**Request:**
```bash
curl -X POST "http://localhost:5000/api/logout" \
-H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Logged out successfully",
    "data": null
}
```

### 9.2 Product Management Additional Endpoints

#### 9.2.1 Get All Categories
Retrieves all unique product categories available in the system.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/categories"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Categories retrieved successfully",
    "data": [
        "Electronics",
        "Clothing",
        "Books",
        "Home & Kitchen"
    ]
}
```

#### 9.2.2 Delete Product Review
Deletes a specific review for a product. The authenticated user must be the owner of the review or an admin.

**Request:**
```bash
curl -X DELETE "http://localhost:5000/api/product/1/review/2" \
-H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Review deleted successfully",
    "data": null
}
```

### 9.3 Order Management Additional Endpoints

#### 9.3.1 Initiate Payment for Order
Initiates payment processing for a specific order.

**Request:**
```bash
curl -X POST "http://localhost:5000/api/order/1/initiate-payment" \
-H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Payment initiated successfully",
    "data": {
        "orderId": "order_123456789",
        "amount": 199998,
        "currency": "INR",
        "receipt": "order_rcptid_1"
    }
}
```

#### 9.3.2 Alternative Order Placement Endpoints

##### 9.3.2.1 Place Order with Form Data
Alternative endpoint for placing orders using form data.

**Request:**
```bash
curl -X POST "http://localhost:5000/api/order-place-form?shippingAddress=123%20Main%20St%2C%20City" \
-H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Order placed successfully",
    "data": {
        "id": 1,
        "items": [...],
        "totalAmount": 1999.98,
        "shippingAddress": "123 Main St, City",
        "status": "PENDING",
        "createdAt": "2024-03-15T10:30:00Z"
    }
}
```

##### 9.3.2.2 Debug Raw Request Body
Debugging endpoint to verify the raw payload received during order placement.

**Request:**
```bash
curl -X POST "http://localhost:5000/api/order-place-debug-raw" \
-H "Authorization: Bearer YOUR_JWT_TOKEN" \
-H "Content-Type: application/json" \
-d "{\"shippingAddress\": \"123 Main St, City\"}"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Received payload",
    "payload": "{\"shippingAddress\": \"123 Main St, City\"}"
}
```

##### 9.3.2.3 Debug Request Details
Debugging endpoint to inspect full request details including headers and content type.

**Request:**
```bash
curl -X POST "http://localhost:5000/api/order-place-debug" \
-H "Authorization: Bearer YOUR_JWT_TOKEN" \
-H "Content-Type: application/json" \
-d "{\"shippingAddress\": \"123 Main St, City\"}"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Debug information logged",
    "data": null
}
```

### 9.4 Admin Order Management Additional Endpoints

#### 9.4.1 Delete Order (Admin)
Allows an administrator to delete an order completely from the system.

**Request:**
```bash
curl -X DELETE "http://localhost:5000/api/admin/order/1" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Order deleted successfully",
    "data": null
}
```

### 9.5 Alternative Order Cancellation

#### 9.5.1 Cancel Order (Alternative Endpoint)
An alternative endpoint to cancel an order.

**Request:**
```bash
curl -X DELETE "http://localhost:5000/api/order-delete/1" \
-H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Example Response:**
```json
{
    "success": true,
    "message": "Order cancelled successfully",
    "data": null
}
```

## Admin Reports API

The following endpoints provide various analytics reports for admin users.

### Generate Sales Report

```
GET /api/admin/reports/sales
```

**Parameters:**
- `start_date` (required): The start date for the report period (ISO-8601 format, e.g., `2023-01-01T00:00:00`)
- `end_date` (required): The end date for the report period (ISO-8601 format, e.g., `2023-01-31T23:59:59`)
- `format` (optional): Report format, default is `JSON`

**Request:**
```bash
curl -X GET "http://localhost:5000/api/admin/reports/sales?start_date=2023-01-01T00:00:00&end_date=2023-01-31T23:59:59&format=JSON" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
  "success": true,
  "message": "Sales report generated successfully",
  "data": {
    "reportName": "Sales Report",
    "reportType": "SALES",
    "generatedAt": "2023-05-15T13:45:30",
    "parameters": {
      "startDate": "2023-01-01T00:00:00",
      "endDate": "2023-01-31T23:59:59"
    },
    "summary": {
      "totalRevenue": 12540.50,
      "totalOrders": 125,
      "averageOrderValue": 100.32,
      "ordersByStatus": {
        "PENDING": 5,
        "CONFIRMED": 10,
        "SHIPPED": 25,
        "DELIVERED": 80,
        "CANCELLED": 5
      }
    },
    "data": [
      {
        "orderId": 1001,
        "orderDate": "2023-01-15T14:30:45",
        "customer": "John Doe",
        "amount": 125.99,
        "status": "DELIVERED"
      },
      // More orders...
    ],
    "format": "JSON"
  }
}
```

### Generate Product Performance Report

```
GET /api/admin/reports/products
```

**Parameters:**
- `start_date` (required): The start date for the report period (ISO-8601 format, e.g., `2023-01-01T00:00:00`)
- `end_date` (required): The end date for the report period (ISO-8601 format, e.g., `2023-01-31T23:59:59`)
- `format` (optional): Report format, default is `JSON`

**Request:**
```bash
curl -X GET "http://localhost:5000/api/admin/reports/products?start_date=2023-01-01T00:00:00&end_date=2023-01-31T23:59:59&format=JSON" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
  "success": true,
  "message": "Product performance report generated successfully",
  "data": {
    "reportName": "Product Performance Report",
    "reportType": "PRODUCT",
    "generatedAt": "2023-05-15T13:45:30",
    "parameters": {
      "startDate": "2023-01-01T00:00:00",
      "endDate": "2023-01-31T23:59:59"
    },
    "summary": {
      "totalProductsSold": 350,
      "totalRevenue": 12540.50,
      "productsWithNoSales": 15,
      "topProducts": [
        {
          "productId": 101,
          "productName": "Premium Headphones",
          "revenue": 2499.95,
          "quantitySold": 25
        },
        // More top products...
      ]
    },
    "data": [
      {
        "productId": 101,
        "productName": "Premium Headphones",
        "category": "Electronics",
        "price": 99.99,
        "currentStock": 45,
        "quantitySold": 25,
        "revenue": 2499.95
      },
      // More products...
    ],
    "format": "JSON"
  }
}
```

### Generate Customer Activity Report

```
GET /api/admin/reports/customers
```

**Parameters:**
- `start_date` (required): The start date for the report period (ISO-8601 format, e.g., `2023-01-01T00:00:00`)
- `end_date` (required): The end date for the report period (ISO-8601 format, e.g., `2023-01-31T23:59:59`)
- `format` (optional): Report format, default is `JSON`

**Request:**
```bash
curl -X GET "http://localhost:5000/api/admin/reports/customers?start_date=2023-01-01T00:00:00&end_date=2023-01-31T23:59:59&format=JSON" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
  "success": true,
  "message": "Customer activity report generated successfully",
  "data": {
    "reportName": "Customer Activity Report",
    "reportType": "CUSTOMER",
    "generatedAt": "2023-05-15T13:45:30",
    "parameters": {
      "startDate": "2023-01-01T00:00:00",
      "endDate": "2023-01-31T23:59:59"
    },
    "summary": {
      "totalCustomers": 250,
      "activeCustomers": 120,
      "inactiveCustomers": 130,
      "totalRevenue": 12540.50,
      "topCustomers": [
        {
          "customerId": 501,
          "customerName": "Jane Smith",
          "totalSpent": 899.95,
          "orderCount": 5
        },
        // More top customers...
      ]
    },
    "data": [
      {
        "customerId": 501,
        "customerName": "Jane Smith",
        "email": "jane.smith@example.com",
        "joinDate": "2022-10-15T09:30:00",
        "orderCount": 5,
        "totalSpent": 899.95,
        "averageOrderValue": 179.99
      },
      // More customers...
    ],
    "format": "JSON"
  }
}
```

### Generate Inventory Status Report

```
GET /api/admin/reports/inventory
```

**Parameters:**
- `format` (optional): Report format, default is `JSON`

**Request:**
```bash
curl -X GET "http://localhost:5000/api/admin/reports/inventory?format=JSON" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
  "success": true,
  "message": "Inventory status report generated successfully",
  "data": {
    "reportName": "Inventory Status Report",
    "reportType": "INVENTORY",
    "generatedAt": "2023-05-15T13:45:30",
    "parameters": {},
    "summary": {
      "totalProducts": 150,
      "outOfStockProducts": 10,
      "lowStockProducts": 25,
      "inventoryValue": 45250.75
    },
    "data": [
      {
        "productId": 101,
        "productName": "Premium Headphones",
        "category": "Electronics",
        "price": 99.99,
        "currentStock": 5,
        "stockStatus": "LOW_STOCK"
      },
      // More products...
    ],
    "format": "JSON"
  }
}
```

### Generate Dashboard Report

```
GET /api/admin/reports/dashboard
```

**Parameters:**
- `format` (optional): Report format, default is `JSON`

**Request:**
```bash
curl -X GET "http://localhost:5000/api/admin/reports/dashboard?format=JSON" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
  "success": true,
  "message": "Dashboard report generated successfully",
  "data": {
    "reportName": "Dashboard Report",
    "reportType": "DASHBOARD",
    "generatedAt": "2023-05-15T13:45:30",
    "parameters": {
      "startDate": "2023-04-15T13:45:30",
      "endDate": "2023-05-15T13:45:30"
    },
    "summary": {
      "totalRevenue": 12540.50,
      "totalOrders": 125,
      "averageOrderValue": 100.32,
      "ordersByStatus": {
        "PENDING": 5,
        "CONFIRMED": 10,
        "SHIPPED": 25,
        "DELIVERED": 80,
        "CANCELLED": 5
      },
      "totalProductsSold": 350,
      "productsWithNoSales": 15,
      "totalCustomers": 250,
      "activeCustomers": 120,
      "inactiveCustomers": 130,
      "totalProducts": 150,
      "outOfStockProducts": 10,
      "lowStockProducts": 25,
      "inventoryValue": 45250.75
    },
    "format": "JSON"
  }
}
```

## 10. Admin API Logging

The API Logging endpoints provide comprehensive tracking of all user actions and API calls in the system. This allows administrators to monitor and audit system usage for security, debugging, and analytics purposes.

### 10.1 Get All API Logs

Retrieves all API logs with pagination.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/admin/logs?page=0&size=20" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
  "success": true,
  "message": "API logs retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "endpoint": "/api/products",
        "method": "GET",
        "userId": 5,
        "userEmail": "user@example.com",
        "userRole": "USER",
        "requestBody": "",
        "responseStatus": 200,
        "ipAddress": "192.168.1.1",
        "userAgent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
        "timestamp": "2023-06-01T13:45:30",
        "executionTime": 125
      }
    ],
    "totalElements": 1250,
    "totalPages": 63,
    "currentPage": 0,
    "size": 20
  }
}
```

### 10.2 Get Logs by User ID

Retrieves API logs for a specific user.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/admin/logs/user/5?page=0&size=20" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
  "success": true,
  "message": "User API logs retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "endpoint": "/api/products",
        "method": "GET",
        "userId": 5,
        "userEmail": "user@example.com",
        "userRole": "USER",
        "requestBody": "",
        "responseStatus": 200,
        "ipAddress": "192.168.1.1",
        "timestamp": "2023-06-01T13:45:30",
        "executionTime": 125
      }
    ],
    "totalElements": 42,
    "totalPages": 3,
    "currentPage": 0,
    "size": 20
  }
}
```

### 10.3 Get Logs by User Email

Retrieves API logs for users matching an email pattern.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/admin/logs/email?email=user@example.com&page=0&size=20" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
  "success": true,
  "message": "Email API logs retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "endpoint": "/api/products",
        "method": "GET",
        "userId": 5,
        "userEmail": "user@example.com",
        "userRole": "USER",
        "requestBody": "",
        "responseStatus": 200,
        "ipAddress": "192.168.1.1",
        "timestamp": "2023-06-01T13:45:30",
        "executionTime": 125
      }
    ],
    "totalElements": 42,
    "totalPages": 3,
    "currentPage": 0,
    "size": 20
  }
}
```

### 10.4 Get Logs by Endpoint

Retrieves API logs for a specific endpoint pattern.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/admin/logs/endpoint?endpoint=/api/products&page=0&size=20" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
  "success": true,
  "message": "Endpoint API logs retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "endpoint": "/api/products",
        "method": "GET",
        "userId": 5,
        "userEmail": "user@example.com",
        "userRole": "USER",
        "requestBody": "",
        "responseStatus": 200,
        "ipAddress": "192.168.1.1",
        "timestamp": "2023-06-01T13:45:30",
        "executionTime": 125
      }
    ],
    "totalElements": 350,
    "totalPages": 18,
    "currentPage": 0,
    "size": 20
  }
}
```

### 10.5 Get Logs by HTTP Method

Retrieves API logs for a specific HTTP method.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/admin/logs/method?method=POST&page=0&size=20" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
  "success": true,
  "message": "Method API logs retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "endpoint": "/api/cart-add/1",
        "method": "POST",
        "userId": 5,
        "userEmail": "user@example.com",
        "userRole": "USER",
        "requestBody": "{\"quantity\": 1}",
        "responseStatus": 200,
        "ipAddress": "192.168.1.1",
        "timestamp": "2023-06-01T13:45:30",
        "executionTime": 150
      }
    ],
    "totalElements": 520,
    "totalPages": 26,
    "currentPage": 0,
    "size": 20
  }
}
```

### 10.6 Get Logs by Date Range

Retrieves API logs within a specific date range.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/admin/logs/date-range?startDate=2023-06-01T00:00:00&endDate=2023-06-02T23:59:59&page=0&size=20" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
  "success": true,
  "message": "Date range API logs retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "endpoint": "/api/products",
        "method": "GET",
        "userId": 5,
        "userEmail": "user@example.com",
        "userRole": "USER",
        "requestBody": "",
        "responseStatus": 200,
        "ipAddress": "192.168.1.1",
        "timestamp": "2023-06-01T13:45:30",
        "executionTime": 125
      }
    ],
    "totalElements": 245,
    "totalPages": 13,
    "currentPage": 0,
    "size": 20
  }
}
```

### 10.7 Get Logs by HTTP Status Code

Retrieves API logs for a specific HTTP status code.

**Request:**
```bash
curl -X GET "http://localhost:5000/api/admin/logs/status?status=404&page=0&size=20" \
-H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

**Example Response:**
```json
{
  "success": true,
  "message": "Status API logs retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "endpoint": "/api/product/999",
        "method": "GET",
        "userId": 5,
        "userEmail": "user@example.com",
        "userRole": "USER",
        "requestBody": "",
        "responseStatus": 404,
        "ipAddress": "192.168.1.1",
        "timestamp": "2023-06-01T13:45:30",
        "executionTime": 75
      }
    ],
    "totalElements": 35,
    "totalPages": 2,
    "currentPage": 0,
    "size": 20
  }
}
```

**Notes:**
- All API logging endpoints are restricted to admin users only
- The system automatically logs all API requests without requiring any action from users
- Request bodies are truncated if they exceed 4000 characters
- Sensitive information like passwords and authentication tokens is automatically redacted from logs
- The execution time is measured in milliseconds
- Logs are stored in the database for a configurable retention period before being automatically purged
