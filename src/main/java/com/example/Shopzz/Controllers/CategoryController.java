package com.example.Shopzz.Controllers;

import com.example.Shopzz.DTO.Request.CategoryCreateRequest;
import com.example.Shopzz.DTO.Request.CategoryUpdateRequest;
import com.example.Shopzz.DTO.Response.CategoryResponse;
import com.example.Shopzz.DTO.Response.PageResponse;
import com.example.Shopzz.Repositories.CategoryRepository;
import com.example.Shopzz.Services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    //GET ALL CATEGORIES
    @GetMapping
    public ResponseEntity<PageResponse<CategoryResponse>> getAll(@PageableDefault(size = 10)Pageable pageable){
        return ResponseEntity.ok(categoryService.getAllCategories(pageable));
    }

    //GET CATEGORY BY ID
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Integer categoryId) {
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }

    //GET CATEGORY BY NAME
    @GetMapping("/name/{categoryName}")
    public ResponseEntity<CategoryResponse> getCategoryByName(@PathVariable String categoryName) {
        return ResponseEntity.ok(categoryService.getCategoryByName(categoryName));
    }

    //GET ALL CATEGORIES BY ACTIVE
    @GetMapping("/active")
    public ResponseEntity<PageResponse<CategoryResponse>> getAllByActive(@RequestParam Boolean active,
                                                                         @PageableDefault(size = 5)Pageable pageable){

        return ResponseEntity.ok(categoryService.getCategoriesByActive(active,pageable));
    }

    //ADD CATEGORY
    @PostMapping
    public ResponseEntity<CategoryResponse> addCategory(@Valid @RequestBody CategoryCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.addCategory(request));
    }

    //DELETE CATEGORY(MEANS PUT ACTIVE OFF)
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer categoryId){
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    //UPDATE CATEGORY
    @PatchMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Integer categoryId,
                                                           @Valid @RequestBody CategoryUpdateRequest request){

        return ResponseEntity.ok(categoryService.updateCategory(categoryId,request));
    }
}
