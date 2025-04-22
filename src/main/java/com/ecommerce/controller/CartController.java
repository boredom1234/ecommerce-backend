package com.ecommerce.controller;

import com.ecommerce.dto.CartDTO;
import com.ecommerce.dto.ApiResponse;
import com.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/cart")
    public ResponseEntity<?> getCartItems() {
        List<CartDTO> cartItems = cartService.getCartItems();
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Cart items retrieved successfully", cartItems));
    }

    @PostMapping("/cart-add/{productId}")
    public ResponseEntity<?> addToCart(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        CartDTO cartItem = cartService.addToCart(productId, quantity);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Item added to cart successfully", cartItem));
    }

    @PatchMapping("/cart-update/{cartId}")
    public ResponseEntity<?> updateCartItem(
            @PathVariable Long cartId,
            @RequestParam Integer quantity) {
        CartDTO cartItem = cartService.updateCartItemById(cartId, quantity);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Cart item updated successfully", cartItem));
    }

    @DeleteMapping("/cart-delete/{cartId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long cartId) {
        cartService.removeCartItemById(cartId);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Item removed from cart successfully", null));
    }
} 