package com.example.Shopzz.DTO.Request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductCreateRequest {

    @NotBlank(message = "product name must not be blank")
    private String productName;

    @Size(max = 500,message = "description max length is 500")
    private String description;

    @NotNull(message = "price must not be null")
    @DecimalMin(value="0.0",inclusive = false, message = "price must be positive")
    private BigDecimal price;

    @NotNull(message = "stock must be not be null")
    @Min(value = 0, message = "stock must be in positive")
    private Integer stock;

    private Boolean active;

    @NotNull(message = "category Id must required")
    private Integer categoryId;
}
