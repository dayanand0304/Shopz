package com.example.Shopzz.Controllers;

import com.example.Shopzz.DTO.Request.CartCreateRequest;
import com.example.Shopzz.DTO.Response.CartResponse;
import com.example.Shopzz.Services.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    //GET CARTS BY USER ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<CartResponse> getByUserId(@PathVariable Integer userId){
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    //GET CART BY CART ID
    @GetMapping("/cart/{cartId}")
    public ResponseEntity<CartResponse> getByCartId(@PathVariable Integer cartId){
        return ResponseEntity.ok(cartService.getCartByCartId(cartId));
    }

    //CREATE CART
    @PostMapping
    public ResponseEntity<CartResponse> createCart(@Valid @RequestBody CartCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.createCart(request.getUserId()));
    }
}
