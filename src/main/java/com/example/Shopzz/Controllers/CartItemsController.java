package com.example.Shopzz.Controllers;

import com.example.Shopzz.DTO.Mapper.CartItemsMapper;
import com.example.Shopzz.DTO.Mapper.CartMapper;
import com.example.Shopzz.DTO.Request.CartItemsCreateRequest;
import com.example.Shopzz.DTO.Response.CartItemsResponse;
import com.example.Shopzz.DTO.Response.CartResponse;
import com.example.Shopzz.Entities.Cart;
import com.example.Shopzz.Services.CartItemsService;
import com.example.Shopzz.Services.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart_items")
@RequiredArgsConstructor
public class CartItemsController {

    private final CartService cartService;
    private final CartItemsService cartItemsService;

    //GET CART ITEMS BY USER ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CartItemsResponse>> getCartItemsByUserId(@PathVariable Integer userId){
        List<CartItemsResponse> items=cartItemsService.getCartItemsByUserId(userId)
                .stream()
                .map(CartItemsMapper::response)
                .toList();
        return ResponseEntity.ok(items);
    }

    //GET CART ITEMS BY CART ID
    @GetMapping("/cart/{cartId}")
    public ResponseEntity<List<CartItemsResponse>> getCartItemsByCartId(@PathVariable Integer cartId){
        List<CartItemsResponse> items=cartItemsService.getCartItemsByCartId(cartId)
                .stream()
                .map(CartItemsMapper::response)
                .toList();
        return ResponseEntity.ok(items);
    }

    //ADD CART ITEMS TO CART
    @PostMapping
    public ResponseEntity<CartResponse> addItemToCart(@Valid @RequestBody CartItemsCreateRequest request){

        Cart cart=cartItemsService.addItemToCart(
                request.getCartId(),
                request.getProductId(),
                request.getQuantity()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(CartMapper.response(cart));
    }


    //INCREASE CART ITEM QUANTITY
    @PutMapping
    public ResponseEntity<CartResponse> increaseQuantity(@Valid @RequestBody CartItemsCreateRequest request){
        Cart cart=cartItemsService.increaseQuantity(
                request.getCartId(),
                request.getProductId(),
                request.getQuantity()
        );
        return ResponseEntity.ok(CartMapper.response(cart));
    }

    //REMOVE ITEMS FROM CART
    @DeleteMapping("/items")
    public ResponseEntity<CartResponse> removeItemFromCart(@RequestParam Integer cartId,
                                                           @RequestParam Integer cartItemId){
        Cart cart=cartItemsService.removeItemFromCart(cartId,cartItemId);
        return ResponseEntity.ok(CartMapper.response(cart));
    }

    //CLEAR CART
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> clearCart(@PathVariable Integer cartId){
        cartItemsService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }

}
