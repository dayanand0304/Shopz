package com.example.Shopzz.Repositories;

import com.example.Shopzz.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Integer> {

    Optional<Category> findByCategoryNameIgnoreCase(String categoryName);

    List<Category> findByActive(Boolean active);

    boolean existsByCategoryNameIgnoreCase(String categoryName);
}
