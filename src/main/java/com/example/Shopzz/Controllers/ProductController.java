package com.example.Shopzz.Controllers;

import com.example.Shopzz.DTO.Request.ProductCreateRequest;
import com.example.Shopzz.DTO.Request.ProductUpdateRequest;
import com.example.Shopzz.DTO.Response.PageResponse;
import com.example.Shopzz.DTO.Response.ProductResponse;
import com.example.Shopzz.Repositories.ProductRepository;
import com.example.Shopzz.Services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    //GET ALL PRODUCTS + FILTERS
    @GetMapping
    public ResponseEntity<PageResponse<ProductResponse>> getAllProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) BigDecimal price,
            @RequestParam(required = false) BigDecimal min,
            @RequestParam(required = false) BigDecimal max,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) Boolean active,
            @PageableDefault(size = 5) Pageable pageable){

        PageResponse<ProductResponse> response;

        if(keyword != null){
            response = productService.searchByName(keyword, pageable);
        }else if(price != null){
            response = productService.getProductByPrice(price, pageable);
        }else if(min != null && max != null){
            response = productService.getProductsByPriceInBetween(min, max, pageable);
        }else if(categoryId != null){
            response = productService.getProductsByCategoryId(categoryId, pageable);
        }else if(categoryName != null){
            response = productService.getProductsByCategoryName(categoryName, pageable);
        }else if(active != null){
            response = productService.getProductsByActiveTrue(pageable);
        }else{
            response = productService.getAllProducts(pageable);
        }

        return ResponseEntity.ok(response);
    }

    //GET PRODUCT BY PRODUCT ID
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Integer productId){
        return ResponseEntity.ok(productService.getProductByProductId(productId));
    }

    //GET PRODUCT BY PRODUCT NAME
    @GetMapping("/name/{productName}")
    public ResponseEntity<ProductResponse> getProductByName(@PathVariable String productName){
        return ResponseEntity.ok(productService.getProductByProductName(productName));
    }

    //ADD PRODUCT
    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody ProductCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.addProduct(request));
    }

    //DELETE PRODUCT
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer productId){
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    //UPDATE PRODUCT
    @PatchMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Integer productId,
                                                         @Valid @RequestBody ProductUpdateRequest request){
        return ResponseEntity.ok(productService.updateProduct(productId,request));
    }
}
