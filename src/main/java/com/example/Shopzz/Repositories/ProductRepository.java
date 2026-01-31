package com.example.Shopzz.Repositories;

import com.example.Shopzz.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {

    Optional<Product> findByProductNameIgnoreCase(String productName);
    boolean existsByProductNameIgnoreCase(String productName);
    List<Product> findByProductNameContainingIgnoreCase(String keyword);

    List<Product> findByPrice(BigDecimal price);
    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);

    List<Product> findByActiveTrue();

    List<Product> findByCategoryCategoryId(Integer categoryId);
    List<Product> findByCategoryCategoryNameIgnoreCase(String categoryName);

}
