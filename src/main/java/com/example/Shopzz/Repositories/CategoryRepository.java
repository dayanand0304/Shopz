package com.example.Shopzz.Repositories;

import com.example.Shopzz.Entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Integer> {

    Optional<Category> findByCategoryNameIgnoreCase(String categoryName);

    Page<Category> findByActive(Boolean active, Pageable pageable);

    boolean existsByCategoryNameIgnoreCase(String categoryName);
}
