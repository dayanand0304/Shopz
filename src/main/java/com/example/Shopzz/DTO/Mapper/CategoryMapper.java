package com.example.Shopzz.DTO.Mapper;

import com.example.Shopzz.DTO.Request.CategoryCreateRequest;
import com.example.Shopzz.DTO.Request.CategoryUpdateRequest;
import com.example.Shopzz.DTO.Response.CategoryResponse;
import com.example.Shopzz.Entities.Category;

public class CategoryMapper {
    public static Category create(CategoryCreateRequest request){
        Category category=new Category();
        category.setCategoryName(request.getCategoryName());
        category.setActive(request.getActive());
        return category;
    }

    public static void update(Category category,CategoryUpdateRequest request){
        category.setCategoryName(request.getCategoryName());
        category.setActive(request.getActive());
    }

    public static CategoryResponse response(Category category){
        return new CategoryResponse(
                category.getCategoryId(),
                category.getCategoryName(),
                category.getActive(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}
