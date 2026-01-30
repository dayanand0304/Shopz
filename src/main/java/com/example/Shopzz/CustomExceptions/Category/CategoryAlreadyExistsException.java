package com.example.Shopzz.CustomExceptions.Category;

public class CategoryAlreadyExistsException extends RuntimeException{
    public CategoryAlreadyExistsException(String name){
        super("Category with name: "+name+" Already Exists");
    }
}
