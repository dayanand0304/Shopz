package com.example.Shopzz.Controllers;

import com.example.Shopzz.DTO.Mapper.CartMapper;
import com.example.Shopzz.DTO.Request.CartItemsCreateRequest;
import com.example.Shopzz.DTO.Response.CartResponse;
import com.example.Shopzz.Entities.Cart;
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

        Cart cart=cartItemsService.addItemToCart(
                request.getCartId(),
                request.getProductId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(CartMapper.response(cart));
    }


    //INCREASE CART ITEM QUANTITY
    @PutMapping("/increase")
    public ResponseEntity<CartResponse> increaseQuantity(@RequestParam Integer cartId,
                                                         @RequestParam Integer productId){
        Cart cart=cartItemsService.increaseQuantity(cartId,productId);
        return ResponseEntity.ok(CartMapper.response(cart));
    }

    //DECREASE QUANTITY OF ITEM
    @PutMapping("/decrease")
    public ResponseEntity<CartResponse> decreaseQuantity(@RequestParam Integer cartId,
                                                         @RequestParam Integer productId){
        Cart cart=cartItemsService.decreaseQuantity(cartId,productId);
        return ResponseEntity.ok(CartMapper.response(cart));
    }

    //REMOVE ITEMS FROM CART
    @DeleteMapping("/items")
    public ResponseEntity<CartResponse> removeItemFromCart(@RequestParam Integer cartId,
                                                           @RequestParam Integer productId){
        Cart cart=cartItemsService.removeItemFromCart(cartId, productId);
        return ResponseEntity.ok(CartMapper.response(cart));
    }

    //CLEAR CART
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> clearCart(@PathVariable Integer cartId){
        cartItemsService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }
}
