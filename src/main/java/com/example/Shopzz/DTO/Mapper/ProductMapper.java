package com.example.Shopzz.DTO.Mapper;

import com.example.Shopzz.DTO.Request.ProductCreateRequest;
import com.example.Shopzz.DTO.Request.ProductUpdateRequest;
import com.example.Shopzz.DTO.Response.ProductResponse;
import com.example.Shopzz.Entities.Category;
import com.example.Shopzz.Entities.Product;

public class ProductMapper {
    public static Product create(ProductCreateRequest request){
        Product products =new Product();

        products.setProductName(request.getProductName());
        products.setDescription(request.getDescription());
        products.setPrice(request.getPrice());
        products.setStock(request.getStock());
        products.setActive(request.getActive());

        Category category=new Category();
        category.setCategoryId(request.getCategoryId());
        products.setCategory(category);
        return products;
    }

    public static Product update(ProductUpdateRequest request){
        Product products =new Product();

        products.setProductName(request.getProductName());
        products.setDescription(request.getDescription());
        products.setPrice(request.getPrice());
        products.setStock(request.getStock());
        products.setActive(request.getActive());

        if(request.getCategoryId()!=null){
            Category category=new Category();
            category.setCategoryId(request.getCategoryId());
            products.setCategory(category);
        }
        return products;
    }
    public static ProductResponse response(Product product){

        Integer categoryId=null;
        if(product.getCategory()!=null){
            categoryId=product.getCategory().getCategoryId();
        }
        return new ProductResponse(
                product.getProductId(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getActive(),
                categoryId,
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
