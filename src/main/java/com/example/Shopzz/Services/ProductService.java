package com.example.Shopzz.Services;

import com.example.Shopzz.Models.Product;
import com.example.Shopzz.Repositries.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    private final Logger log=LoggerFactory.getLogger(ProductService.class);

    //GET ALL PRODUCTS
    public List<Product> getAllProducts(){
        return productRepository.findAll(Sort.by(Sort.Direction.ASC,"productName"));
    }

    //GET PRODUCT BY PRODUCT ID
    public Optional<Product> getProductByProductId(Integer productId){
        return productRepository.findById(productId);
    }

    //GET PRODUCT BY PRODUCT NAME
    public Optional<Product> getProductByProductName(String productName){
        return productRepository.findByProductName(productName);
    }

    //GET PRODUCTS BY CATEGORY
    public List<Product> getProductsByCategory(String category){
        return productRepository.findByCategory(category);
    }

    //ADD PRODUCT
    public Product addProduct(Product product){
        Product saved=productRepository.save(product);
        log.info("Added Product id{}  name{}", saved.getProductId(),saved.getProductName());
        return saved;
    }

    //DELETE PRODUCT BY PRODUCT ID
    public String deleteProductByProductId(Integer id) throws ConfigDataResourceNotFoundException {
        if(productRepository.existsById(id)){
            productRepository.deleteById(id);
            log.info("Deleted Product id={}", id);
        }
        log.warn("Attempted to delete non-existing product id={}", id);
        return ("Product id with " + id + "doesn't exists");
    }

    //UPDATE PRODUCT BY PRODUCT ID
    public Product updateProductByProductId(Integer id, Product updatedProduct) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    if (updatedProduct.getProductName() != null) {
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
                        existingProduct.setCategory(updatedProduct.getCategory());
                    }
                    Product saved=productRepository.save(existingProduct);
                    log.info("Updated Product id={}", saved.getProductId());
                    return saved;
                })
                .orElse(null);
    }
}
