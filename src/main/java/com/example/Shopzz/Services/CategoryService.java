package com.example.Shopzz.Services;

import com.example.Shopzz.CustomExceptions.Category.CategoryAlreadyExistsException;
import com.example.Shopzz.CustomExceptions.Category.CategoryNotFoundException;
import com.example.Shopzz.Entities.Category;
import com.example.Shopzz.Repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    //GET ALL CATEGORIES
    public List<Category> getAllCategories(){
        return categoryRepository.findAll(Sort.by("categoryName"));
    }

    //GET CATEGORY BY ID
    public Category getCategoryById(Integer categoryId){
        return categoryRepository.findById(categoryId)
                .orElseThrow(()->new CategoryNotFoundException(categoryId));
    }

    //GET CATEGORY WITH NAME
    public Category getCategoryByName(String categoryName){
        return categoryRepository.findByCategoryNameIgnoreCase(categoryName)
                .orElseThrow(()->new CategoryNotFoundException(categoryName));
    }

    //GET CATEGORIES BY ACTIVE
    public List<Category> getCategoriesByActive(Boolean active){
        return categoryRepository.findByActive(active);
    }

    //ADD CATEGORY
    public Category addCategory(Category category){
        if(categoryRepository.existsByCategoryNameIgnoreCase(category.getCategoryName())){
            throw new CategoryAlreadyExistsException(category.getCategoryName());
        }
        return categoryRepository.save(category);
    }

    //DELETE CATEGORY
    public void deleteCategory(Integer categoryId){
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()->new CategoryNotFoundException(categoryId));

        category.setActive(false);
        categoryRepository.save(category);
    }

    //UPDATE CATEGORY
    public Category updateCategory(Integer categoryId,Category updated){
        Category existing=getCategoryById(categoryId);

        if(updated.getCategoryName()!=null){
            if(!updated.getCategoryName().equalsIgnoreCase(existing.getCategoryName()) &&
                    categoryRepository.existsByCategoryNameIgnoreCase(updated.getCategoryName())){
                throw new CategoryAlreadyExistsException(updated.getCategoryName());
            }
            existing.setCategoryName(updated.getCategoryName());
        }
        if(updated.getActive()!=null){
            existing.setActive(updated.getActive());
        }
        return categoryRepository.save(existing);
    }
}
