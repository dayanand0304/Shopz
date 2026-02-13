package com.example.Shopzz.Repositories;

import com.example.Shopzz.Entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {

    Optional<Product> findByProductNameIgnoreCase(String productName);
    boolean existsByProductNameIgnoreCase(String productName);

    Page<Product> findByProductNameContainingIgnoreCase(String keyword, Pageable pageable);

    Page<Product> findByPrice(BigDecimal price, Pageable pageable);
    Page<Product> findByPriceBetween(BigDecimal min, BigDecimal max, Pageable pageable);

    Page<Product> findByActiveTrue(Pageable pageable);

    Page<Product> findByCategoryCategoryId(Integer categoryId, Pageable pageable);
    Page<Product> findByCategoryCategoryNameIgnoreCase(String categoryName, Pageable pageable);
}
