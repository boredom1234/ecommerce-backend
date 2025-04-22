package com.ecommerce.controller;

import com.ecommerce.dto.ApiResponse;
import com.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private ProductService productService;

    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
        // Get all products and extract unique categories
        List<String> categories = productService.getAllProducts(null)
                .getContent()
                .stream()
                .map(product -> product.getCategory())
                .filter(category -> category != null && !category.isEmpty())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Categories retrieved successfully", categories));
    }
} 