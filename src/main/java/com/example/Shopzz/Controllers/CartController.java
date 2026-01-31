package com.example.Shopzz.Controllers;

import com.example.Shopzz.DTO.Mapper.CartMapper;
import com.example.Shopzz.DTO.Request.CartCreateRequest;
import com.example.Shopzz.DTO.Response.CartResponse;
import com.example.Shopzz.Entities.Cart;
import com.example.Shopzz.Services.CartService;
import com.example.Shopzz.Services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    //GET ALL CARTS OF ALL USERS
    @GetMapping
    public ResponseEntity<List<CartResponse>> getAll(){
        List<CartResponse> carts=cartService.getAllCarts()
                .stream()
                .map(CartMapper::response)
                .toList();
        return ResponseEntity.ok(carts);
    }

    //GET CARTS BY USER ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<CartResponse> getByUserId(@PathVariable Integer userId){
        Cart cart=cartService.getCartByUserId(userId);
        return ResponseEntity.ok(CartMapper.response(cart));
    }

    //GET CART BY CART ID
    @GetMapping("/{cartId}")
    public ResponseEntity<CartResponse> getByCartId(@PathVariable Integer cartId){
        Cart cart=cartService.getCartByCartId(cartId);
        return ResponseEntity.ok(CartMapper.response(cart));
    }

    //CREATE CART
    @PostMapping
    public ResponseEntity<CartResponse> createCart(@Valid @RequestBody CartCreateRequest request){
        Cart cart=cartService.createCart(
                request.getUserId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(CartMapper.response(cart));
    }

    //DELETE CART BY CART ID
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable Integer cartId){
        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }
}
