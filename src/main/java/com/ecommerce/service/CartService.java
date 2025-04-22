package com.ecommerce.service;

import com.ecommerce.dto.CartDTO;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public List<CartDTO> getCartItems() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cartRepository.findByUserId(user.getId()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CartDTO addToCart(Long productId, Integer quantity) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(quantity);
        cart.setPrice(product.getPrice());

        cart = cartRepository.save(cart);
        return convertToDTO(cart);
    }

    @Transactional
    public CartDTO updateCartItem(Long productId, Integer quantity) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUserId(user.getId()).stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cart.setQuantity(quantity);
        cart = cartRepository.save(cart);
        return convertToDTO(cart);
    }

    @Transactional
    public void removeFromCart(Long productId) {
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // First check if the item exists in the cart
            boolean itemExists = cartRepository.findByUserId(user.getId()).stream()
                    .anyMatch(item -> item.getProduct().getId().equals(productId));
            
            if (!itemExists) {
                throw new RuntimeException("Cart item with productId " + productId + " not found for user " + user.getId());
            }
            
            cartRepository.deleteByUserIdAndProductId(user.getId(), productId);
            
            // Flush to ensure the delete is immediately executed
            cartRepository.flush();
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Error removing item from cart: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional
    public void removeCartItemById(Long cartId) {
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Find the cart item and verify it belongs to this user
            Cart cartItem = cartRepository.findById(cartId)
                    .orElseThrow(() -> new RuntimeException("Cart item not found"));
            
            // Verify this cart item belongs to the current user
            if (!cartItem.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("Not authorized to delete this cart item");
            }
            
            // Delete the cart item directly by ID
            cartRepository.deleteById(cartId);
            
            // Flush to ensure the delete is immediately executed
            cartRepository.flush();
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Error removing item from cart: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional
    public CartDTO updateCartItemById(Long cartId, Integer quantity) {
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Find the cart item and verify it belongs to this user
            Cart cartItem = cartRepository.findById(cartId)
                    .orElseThrow(() -> new RuntimeException("Cart item not found"));
            
            // Verify this cart item belongs to the current user
            if (!cartItem.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("Not authorized to update this cart item");
            }
            
            // Update the quantity
            cartItem.setQuantity(quantity);
            cartItem = cartRepository.save(cartItem);
            
            return convertToDTO(cartItem);
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Error updating cart item: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private CartDTO convertToDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setProductId(cart.getProduct().getId());
        dto.setProductName(cart.getProduct().getName());
        dto.setProductImage(cart.getProduct().getImageUrl());
        dto.setQuantity(cart.getQuantity());
        dto.setPrice(cart.getPrice());
        dto.setTotalPrice(cart.getPrice().multiply(java.math.BigDecimal.valueOf(cart.getQuantity())));
        return dto;
    }
} 