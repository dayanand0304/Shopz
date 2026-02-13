package com.example.Shopzz.Services;

import com.example.Shopzz.CustomExceptions.Category.CategoryNotFoundException;
import com.example.Shopzz.CustomExceptions.Products.ProductAlreadyExistsException;
import com.example.Shopzz.CustomExceptions.Products.ProductNotFoundException;
import com.example.Shopzz.DTO.Mapper.ProductMapper;
import com.example.Shopzz.DTO.PageMapper;
import com.example.Shopzz.DTO.Request.ProductCreateRequest;
import com.example.Shopzz.DTO.Request.ProductUpdateRequest;
import com.example.Shopzz.DTO.Response.PageResponse;
import com.example.Shopzz.DTO.Response.ProductResponse;
import com.example.Shopzz.Entities.Category;
import com.example.Shopzz.Entities.Product;
import com.example.Shopzz.Repositories.CategoryRepository;
import com.example.Shopzz.Repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    //GET ALL PRODUCTS
    public PageResponse<ProductResponse> getAllProducts(Pageable pageable){
        Page<Product> page = productRepository.findAll(pageable);
        return PageMapper.toPageResponse(page, ProductMapper::response);
    }

    //GET PRODUCT BY PRODUCT ID
    public ProductResponse getProductByProductId(Integer productId){
        Product product=productRepository.findById(productId)
                .orElseThrow(()->new ProductNotFoundException(productId));
        return ProductMapper.response(product);
    }

    //GET PRODUCT BY PRODUCT NAME
    public ProductResponse getProductByProductName(String productName){
        Product product=productRepository.findByProductNameIgnoreCase(productName)
                .orElseThrow(()->new ProductNotFoundException(productName));
        return ProductMapper.response(product);
    }

    //SEARCH PRODUCTS BY NAME
    public PageResponse<ProductResponse> searchByName(String keyword, Pageable pageable){
        Page<Product> page = productRepository.findByProductNameContainingIgnoreCase(keyword, pageable);
        return PageMapper.toPageResponse(page, ProductMapper::response);
    }

    //GET PRODUCTS BY PRICE
    public PageResponse<ProductResponse> getProductByPrice(BigDecimal price, Pageable pageable){
        Page<Product> page = productRepository.findByPrice(price, pageable);
        return PageMapper.toPageResponse(page, ProductMapper::response);
    }

    //GET PRODUCTS BETWEEN MIN AND MAX PRICE
    public PageResponse<ProductResponse> getProductsByPriceInBetween(BigDecimal min, BigDecimal max, Pageable pageable){
        Page<Product> page = productRepository.findByPriceBetween(min, max, pageable);
        return PageMapper.toPageResponse(page, ProductMapper::response);
    }

    //GET PRODUCTS BY ACTIVE
    public PageResponse<ProductResponse> getProductsByActiveTrue(Pageable pageable){
        Page<Product> page = productRepository.findByActiveTrue(pageable);
        return PageMapper.toPageResponse(page, ProductMapper::response);
    }

    //GET PRODUCTS BY CATEGORY ID
    public PageResponse<ProductResponse> getProductsByCategoryId(Integer categoryId, Pageable pageable){
        if(!categoryRepository.existsById(categoryId)){
            throw new CategoryNotFoundException(categoryId);
        }
        Page<Product> page = productRepository.findByCategoryCategoryId(categoryId, pageable);
        return PageMapper.toPageResponse(page, ProductMapper::response);
    }

    //GET PRODUCTS BY CATEGORY NAME
    public PageResponse<ProductResponse> getProductsByCategoryName(String categoryName, Pageable pageable){
        Page<Product> page = productRepository.findByCategoryCategoryNameIgnoreCase(categoryName, pageable);
        return PageMapper.toPageResponse(page, ProductMapper::response);
    }

    //ADD PRODUCT
    public ProductResponse addProduct(ProductCreateRequest request){
        if(productRepository.existsByProductNameIgnoreCase(request.getProductName())){
            throw new ProductAlreadyExistsException(request.getProductName());
        }
        Integer catId=request.getCategoryId();
        Category category=categoryRepository.findById(catId)
                .orElseThrow(()->new CategoryNotFoundException(catId));

        Product product=ProductMapper.create(request);

        product.setCategory(category);

        Product saved=productRepository.save(product);

        return ProductMapper.response(saved);
    }

    //DELETE PRODUCT BY PRODUCT ID
    @Transactional
    public void deleteProduct(Integer productId){
        Product product=productRepository.findById(productId)
                .orElseThrow(()->new ProductNotFoundException(productId));
        product.setActive(false);
        productRepository.save(product);
    }

    //UPDATE PRODUCT BY PRODUCT ID
    @Transactional
    public ProductResponse updateProduct(Integer id, ProductUpdateRequest request) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    if (request.getProductName() != null) {
                        if (!request.getProductName().equalsIgnoreCase(existingProduct.getProductName()) &&
                                productRepository.existsByProductNameIgnoreCase(request.getProductName())) {
                            throw new ProductAlreadyExistsException(request.getProductName());
                        }
                        existingProduct.setProductName(request.getProductName());
                    }
                    if (request.getDescription() != null) {
                        existingProduct.setDescription(request.getDescription());
                    }
                    if (request.getPrice() != null) {
                        existingProduct.setPrice(request.getPrice());
                    }
                    if (request.getStock() != null) {
                        existingProduct.setStock(request.getStock());
                    }
                    if (request.getCategoryId() != null) {
                        Integer catId=request.getCategoryId();
                        Category category=categoryRepository.findById(catId)
                                .orElseThrow(()->new CategoryNotFoundException(catId));
                        existingProduct.setCategory(category);
                    }
                    if(request.getActive()!=null){
                        existingProduct.setActive(request.getActive());
                    }
                    Product updated=productRepository.save(existingProduct);

                    return ProductMapper.response(updated);
                })
                .orElseThrow(()->new ProductNotFoundException(id));
    }
}
