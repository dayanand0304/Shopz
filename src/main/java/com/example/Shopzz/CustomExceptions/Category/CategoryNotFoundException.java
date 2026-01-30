package com.example.Shopzz.CustomExceptions.Category;

public class CategoryNotFoundException extends RuntimeException{
    public CategoryNotFoundException(Integer id){
        super("Category with Id: "+id+" not found");
    }

    public CategoryNotFoundException(String name){
        super("Category with "+name+" not found");
    }

    public CategoryNotFoundException(Boolean active){
        super("Category with active: "+active+" not found");
    }
}
