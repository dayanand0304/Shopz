package com.example.Shopzz.CustomExceptions.Products;

import java.math.BigDecimal;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(Integer productId){
        super("Product with Id: "+productId+" not found");
    }

    public ProductNotFoundException(String name){
        super("Product with name: "+name+" not found");
    }

    public ProductNotFoundException(BigDecimal price){
        super("No Products found with price: "+price);
    }

    public ProductNotFoundException(BigDecimal min,BigDecimal max){
        super("No products found between price "+min+" and "+max);
    }

    public ProductNotFoundException(){
        super("No products found");
    }
}
