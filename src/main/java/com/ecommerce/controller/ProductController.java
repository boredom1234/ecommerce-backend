package com.ecommerce.controller;

import com.ecommerce.dto.ProductDTO;
import com.ecommerce.dto.ReviewDTO;
import com.ecommerce.dto.ApiResponse;
import com.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ProductDTO> products = productService.getAllProducts(PageRequest.of(page, size));
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Products retrieved successfully", products));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        ProductDTO product = productService.getProduct(id);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Product retrieved successfully", product));
    }

    @GetMapping("/product-search")
    public ResponseEntity<?> searchProducts(@RequestParam String query) {
        List<ProductDTO> products = productService.searchProducts(query);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Products retrieved successfully", products));
    }

    @GetMapping("/products/category/{category}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable String category) {
        List<ProductDTO> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Products retrieved successfully", products));
    }

    @PostMapping("/product/{id}/review")
    public ResponseEntity<?> addReview(
            @PathVariable Long id,
            @RequestParam Integer rating,
            @RequestParam String comment) {
        ReviewDTO review = productService.addReview(id, rating, comment);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Review added successfully", review));
    }

    @GetMapping("/product/{id}/reviews")
    public ResponseEntity<?> getProductReviews(@PathVariable Long id) {
        List<ReviewDTO> reviews = productService.getProductReviews(id);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Reviews retrieved successfully", reviews));
    }

    @DeleteMapping("/product/{id}/review/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @PathVariable Long id,
            @PathVariable Long reviewId) {
        productService.deleteReview(id, reviewId);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Review deleted successfully", null));
    }
} 