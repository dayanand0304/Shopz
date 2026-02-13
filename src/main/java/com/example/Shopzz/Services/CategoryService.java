package com.example.Shopzz.Services;

import com.example.Shopzz.CustomExceptions.Category.CategoryAlreadyExistsException;
import com.example.Shopzz.CustomExceptions.Category.CategoryNotFoundException;
import com.example.Shopzz.DTO.Mapper.CategoryMapper;
import com.example.Shopzz.DTO.PageMapper;
import com.example.Shopzz.DTO.Request.CategoryCreateRequest;
import com.example.Shopzz.DTO.Request.CategoryUpdateRequest;
import com.example.Shopzz.DTO.Response.CategoryResponse;
import com.example.Shopzz.DTO.Response.PageResponse;
import com.example.Shopzz.Entities.Category;
import com.example.Shopzz.Repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    //GET ALL CATEGORIES
    public PageResponse<CategoryResponse> getAllCategories(Pageable pageable){

        Page<Category> page=categoryRepository.findAll(pageable);

        return PageMapper.toPageResponse(page, CategoryMapper::response);
    }

    //GET CATEGORY BY ID
    public CategoryResponse getCategoryById(Integer categoryId){
        Category category= categoryRepository.findById(categoryId)
                .orElseThrow(()->new CategoryNotFoundException(categoryId));

        return CategoryMapper.response(category);
    }

    //GET CATEGORY WITH NAME
    public CategoryResponse getCategoryByName(String categoryName){
        Category category=categoryRepository.findByCategoryNameIgnoreCase(categoryName)
                .orElseThrow(()->new CategoryNotFoundException(categoryName));

        return CategoryMapper.response(category);
    }

    //GET CATEGORIES BY ACTIVE
    public PageResponse<CategoryResponse> getCategoriesByActive(Boolean active,Pageable pageable){
        Page<Category> page=categoryRepository.findByActive(active,pageable);

        return PageMapper.toPageResponse(page,CategoryMapper::response);
    }

    //ADD CATEGORY
    public CategoryResponse addCategory(CategoryCreateRequest request){
        if(categoryRepository.existsByCategoryNameIgnoreCase(request.getCategoryName())){
            throw new CategoryAlreadyExistsException(request.getCategoryName());
        }
        Category category=CategoryMapper.create(request);
        Category saved=categoryRepository.save(category);

        return CategoryMapper.response(saved);
    }

    //DELETE CATEGORY
    public void deleteCategory(Integer categoryId){
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()->new CategoryNotFoundException(categoryId));

        category.setActive(false);
        categoryRepository.save(category);
    }

    //UPDATE CATEGORY
    public CategoryResponse updateCategory(Integer categoryId, CategoryUpdateRequest request){
        return categoryRepository.findById(categoryId)
                .map(existing->{
                    if(request.getCategoryName()!=null){
                        if(!request.getCategoryName().equalsIgnoreCase(existing.getCategoryName()) &&
                                categoryRepository.existsByCategoryNameIgnoreCase(request.getCategoryName())){
                            throw new CategoryAlreadyExistsException(request.getCategoryName());
                        }
                        existing.setCategoryName(request.getCategoryName());
                    }
                    if(request.getActive()!=null){
                        existing.setActive(request.getActive());
                    }
                    Category updated=categoryRepository.save(existing);

                    return CategoryMapper.response(updated);
                })
                .orElseThrow(()->new CategoryNotFoundException(categoryId));
    }
}
