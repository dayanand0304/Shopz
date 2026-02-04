package com.example.Shopzz.Controllers;

import com.example.Shopzz.CustomExceptions.Products.ProductNotFoundException;
import com.example.Shopzz.DTO.Mapper.ProductMapper;
import com.example.Shopzz.DTO.Request.ProductCreateRequest;
import com.example.Shopzz.DTO.Request.ProductUpdateRequest;
import com.example.Shopzz.DTO.Response.ProductResponse;
import com.example.Shopzz.Entities.Product;
import com.example.Shopzz.Repositories.ProductRepository;
import com.example.Shopzz.Services.CategoryService;
import com.example.Shopzz.Services.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(){
        List<ProductResponse> products=productService.getAllProducts()
                .stream()
                .map(ProductMapper::response)
                .toList();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Integer productId){
        Product product=productService.getProductByProductId(productId);
        return ResponseEntity.ok(ProductMapper.response(product));
    }

    @GetMapping("/name/{productName}")
    public ResponseEntity<ProductResponse> getProductByName(@PathVariable String productName){
        Product product=productService.getProductByProductName(productName);
        return ResponseEntity.ok(ProductMapper.response(product));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchByNameContaining(@RequestParam String keyword){
        List<ProductResponse> products=productService.searchByName(keyword)
                .stream()
                .map(ProductMapper::response)
                .toList();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/price")
    public ResponseEntity<List<ProductResponse>> getProductsByPrice(@RequestParam @DecimalMin(value="0.0", inclusive=false) BigDecimal price){
        List<ProductResponse> products=productService.getProductByPrice(price)
                .stream()
                .map(ProductMapper::response)
                .toList();

        return ResponseEntity.ok(products);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<ProductResponse>> getProductsByPriceRange(@RequestParam @DecimalMin(value="0.0", inclusive=false) BigDecimal min,
                                                                         @RequestParam @DecimalMin(value="0.0", inclusive=false) BigDecimal max){
        List<ProductResponse> products=productService.getProductsByPriceInBetween(min, max)
                .stream()
                .map(ProductMapper::response)
                .toList();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/active")
    public ResponseEntity<List<ProductResponse>> getProductsByActiveTrue(){
        List<ProductResponse> products=productService.getProductsByActiveTrue()
                .stream()
                .map(ProductMapper::response)
                .toList();

        return ResponseEntity.ok(products);
    }

    @GetMapping("/category-id")
    public ResponseEntity<List<ProductResponse>> getProductsByCategoryId(@RequestParam Integer categoryId){
        List<ProductResponse> products=productService.getProductsByCategoryId(categoryId)
                .stream()
                .map(ProductMapper::response)
                .toList();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category-name")
    public ResponseEntity<List<ProductResponse>> getProductsByCategoryName(@RequestParam String categoryName){
        List<ProductResponse> products=productService.getProductsByCategoryName(categoryName)
                .stream()
                .map(ProductMapper::response)
                .toList();

        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody ProductCreateRequest request){
        Product product=ProductMapper.create(request);
        Product saved=productService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductMapper.response(saved));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer productId){
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Integer productId,
                                                         @Valid @RequestBody ProductUpdateRequest request){
        Product product=productRepository.findById(productId)
                .orElseThrow(()->new ProductNotFoundException(productId));

        ProductMapper.update(product,request);
        Product updated=productService.updateProduct(productId,product);
        return ResponseEntity.ok(ProductMapper.response(updated));
    }
}
