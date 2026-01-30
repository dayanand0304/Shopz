package com.example.Shopzz.Controllers;

import com.example.Shopzz.DTO.CategoryMapper;
import com.example.Shopzz.DTO.Request.CategoryCreateRequest;
import com.example.Shopzz.DTO.Request.CategoryUpdateRequest;
import com.example.Shopzz.DTO.Response.CategoryResponse;
import com.example.Shopzz.Models.Category;
import com.example.Shopzz.Services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    //GET ALL CATEGORIES
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll(){
        List<CategoryResponse> categories=categoryService.getAllCategories()
                .stream()
                .map(CategoryMapper::response)
                .toList();
        return ResponseEntity.ok(categories);
    }

    //GET CATEGORY BY ID
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Integer categoryId) {
        Category category=categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(CategoryMapper.response(category));
    }

    //GET CATEGORY BY NAME
    @GetMapping("/name/{categoryName}")
    public ResponseEntity<CategoryResponse> getCategoryByName(@PathVariable String categoryName) {
        Category category=categoryService.getCategoryByName(categoryName);
        return ResponseEntity.ok(CategoryMapper.response(category));
    }

    //GET ALL CATEGORIES BY ACTIVE
    @GetMapping("/active")
    public ResponseEntity<List<CategoryResponse>> getAllByActive(@RequestParam Boolean active){
        List<CategoryResponse> categories=categoryService.getCategoriesByActive(active)
                .stream()
                .map(CategoryMapper::response)
                .toList();
        return ResponseEntity.ok(categories);
    }

    //ADD CATEGORY
    @PostMapping
    public ResponseEntity<CategoryResponse> addCategory(@Valid @RequestBody CategoryCreateRequest request){
        Category category=CategoryMapper.create(request);
        Category saved=categoryService.addCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(CategoryMapper.response(saved));
    }

    //DELETE CATEGORY(MEANS PUT ACTIVE OFF)
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer categoryId){
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    //UPDATE CATEGORY
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Integer categoryId,
                                                           @Valid @RequestBody CategoryUpdateRequest request){
        Category category=CategoryMapper.update(request);
        Category updated=categoryService.updateCategory(categoryId,category);
        return ResponseEntity.ok(CategoryMapper.response(updated));
    }
}
