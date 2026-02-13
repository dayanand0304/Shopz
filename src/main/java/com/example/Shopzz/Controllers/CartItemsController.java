package com.example.Shopzz.Controllers;

import com.example.Shopzz.DTO.Request.CartItemsCreateRequest;
import com.example.Shopzz.DTO.Response.CartResponse;
import com.example.Shopzz.Services.CartItemsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart-items")
@RequiredArgsConstructor
public class CartItemsController {

    private final CartItemsService cartItemsService;

    //ADD CART ITEMS TO CART
    @PostMapping
    public ResponseEntity<CartResponse> addItemToCart(@Valid @RequestBody CartItemsCreateRequest request){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartItemsService.addItemToCart(request.getCartId(),request.getProductId()));
    }


    //INCREASE CART ITEM QUANTITY
    @PutMapping("/increase")
    public ResponseEntity<CartResponse> increaseQuantity(@RequestParam Integer cartId,
                                                         @RequestParam Integer productId){
        return ResponseEntity.ok(cartItemsService.increaseQuantity(cartId,productId));
    }

    //DECREASE QUANTITY OF ITEM
    @PutMapping("/decrease")
    public ResponseEntity<CartResponse> decreaseQuantity(@RequestParam Integer cartId,
                                                         @RequestParam Integer productId){
        return ResponseEntity.ok(cartItemsService.decreaseQuantity(cartId,productId));
    }

    //REMOVE ITEMS FROM CART
    @DeleteMapping("/items")
    public ResponseEntity<CartResponse> removeItemFromCart(@RequestParam Integer cartId,
                                                           @RequestParam Integer productId){
        return ResponseEntity.ok(cartItemsService.removeItemFromCart(cartId,productId));
    }

    //CLEAR CART
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> clearCart(@PathVariable Integer cartId){
        cartItemsService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }
}
