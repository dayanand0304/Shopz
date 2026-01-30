package com.example.Shopzz.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryCreateRequest {

    @NotBlank(message = "category name must not be blank")
    @Size(min = 3,max = 100,message = "category must be 3-100 characters")
    private String categoryName;

    @NotNull(message = "active must not be null")
    private Boolean active;
}
