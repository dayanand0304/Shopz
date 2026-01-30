package com.example.Shopzz.DTO.Request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryUpdateRequest {

    @Size(min = 3, max = 100, message = "Category must be 3-100 characters")
    private String categoryName;

    private Boolean active;
}
