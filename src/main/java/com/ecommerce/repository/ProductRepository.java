package com.ecommerce.repository;

import com.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Map;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Product> searchProducts(String query);

    List<Product> findByCategory(String category);
    
    List<Product> findByStockLessThanEqual(Integer threshold);
    
    /**
     * Find top selling products based on order quantities
     * Returns the top 10 products by default
     * 
     * @return List of maps containing product data and sales data
     */
    @Query(value = "SELECT p.id as id, p.name as name, p.image_url as imageUrl, " +
                  "SUM(oi.quantity) as totalQuantity, " +
                  "SUM(oi.price * oi.quantity) as totalRevenue " +
                  "FROM products p " +
                  "JOIN order_items oi ON p.id = oi.product_id " +
                  "JOIN orders o ON oi.order_id = o.id " +
                  "WHERE o.status != 'CANCELLED' " +
                  "GROUP BY p.id, p.name, p.image_url " +
                  "ORDER BY totalQuantity DESC " +
                  "LIMIT 10", nativeQuery = true)
    List<Map<String, Object>> findTopSellingProducts();
} 