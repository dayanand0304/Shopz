package com.example.Shopzz.Services;

import com.example.Shopzz.CustomExceptions.Category.CategoryNotFoundException;
import com.example.Shopzz.CustomExceptions.Products.ProductAlreadyExistsException;
import com.example.Shopzz.CustomExceptions.Products.ProductNotFoundException;
import com.example.Shopzz.Entities.Category;
import com.example.Shopzz.Entities.Product;
import com.example.Shopzz.Repositories.CategoryRepository;
import com.example.Shopzz.Repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    //GET ALL PRODUCTS
    public List<Product> getAllProducts(){
        return productRepository.findAll(Sort.by(Sort.Direction.ASC,"productName"));
    }

    //GET PRODUCT BY PRODUCT ID
    public Product getProductByProductId(Integer productId){
        return productRepository.findById(productId)
                .orElseThrow(()->new ProductNotFoundException(productId));
    }

    //GET PRODUCT BY PRODUCT NAME
    public Product getProductByProductName(String productName){
        return productRepository.findByProductNameIgnoreCase(productName)
                .orElseThrow(()->new ProductNotFoundException(productName));
    }

    //SEARCH PRODUCTS BY NAME
    public List<Product> searchByName(String keyword){
        return productRepository.findByProductNameContainingIgnoreCase(keyword);
    }

    //GET PRODUCTS BY PRICE
    public List<Product> getProductByPrice(BigDecimal price){
        return productRepository.findByPrice(price);
    }

    //GET PRODUCTS BY BETWEEN MIN AND MAX PRICE
    public List<Product> getProductsByPriceInBetween(BigDecimal min,BigDecimal max){
        List<Product> products=productRepository.findByPriceBetween(min,max);
        if(products.isEmpty()){
            throw new ProductNotFoundException(min,max);
        }
        return products;
    }

    //GET PRODUCTS BY ACTIVE
    public List<Product> getProductsByActiveTrue(){
        return productRepository.findByActiveTrue();
    }

    //GET PRODUCTS BY CATEGORY
    public List<Product> getProductsByCategoryId(Integer categoryId){
        if(!categoryRepository.existsById(categoryId)){
            throw new CategoryNotFoundException(categoryId);
        }
        List<Product> products=productRepository.findByCategoryCategoryId(categoryId);
        if(products.isEmpty()){
            throw new ProductNotFoundException("There is no products with category id: "+categoryId);
        }
        return products;
    }

    //GET PRODUCTS BY CATEGORY NAME
    public List<Product> getProductsByCategoryName(String categoryName){
        List<Product> products=productRepository.findByCategoryCategoryNameIgnoreCase(categoryName);
        if(products.isEmpty()){
            throw new ProductNotFoundException("There is no product with category name: "+categoryName);
        }
        return products;
    }

    //ADD PRODUCT
    public Product addProduct(Product product){
        if(productRepository.existsByProductNameIgnoreCase(product.getProductName())){
            throw new ProductAlreadyExistsException(product.getProductName());
        }
        Integer catId=product.getCategory().getCategoryId();
        Category category=categoryRepository.findById(catId)
                .orElseThrow(()->new CategoryNotFoundException(catId));

        product.setCategory(category);
        return productRepository.save(product);
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
    public Product updateProduct(Integer id, Product updatedProduct) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    if (updatedProduct.getProductName() != null) {
                        if (!updatedProduct.getProductName().equalsIgnoreCase(existingProduct.getProductName()) &&
                                productRepository.existsByProductNameIgnoreCase(updatedProduct.getProductName())) {
                            throw new ProductAlreadyExistsException(updatedProduct.getProductName());

                        }
                        existingProduct.setProductName(updatedProduct.getProductName());
                    }
                    if (updatedProduct.getDescription() != null) {
                        existingProduct.setDescription(updatedProduct.getDescription());
                    }
                    if (updatedProduct.getPrice() != null) {
                        existingProduct.setPrice(updatedProduct.getPrice());
                    }
                    if (updatedProduct.getStock() != null) {
                        existingProduct.setStock(updatedProduct.getStock());
                    }
                    if (updatedProduct.getCategory() != null) {
                        Integer catId=updatedProduct.getCategory().getCategoryId();
                        Category category=categoryRepository.findById(catId)
                                        .orElseThrow(()->new CategoryNotFoundException(catId));
                        existingProduct.setCategory(updatedProduct.getCategory());
                    }
                    return productRepository.save(existingProduct);
                })
                .orElseThrow(()->new ProductNotFoundException(id));
    }
}
