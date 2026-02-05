package com.example.Shopzz.Services;

import com.example.Shopzz.CustomExceptions.CartAndCartItems.*;
import com.example.Shopzz.CustomExceptions.Products.ProductNotFoundException;
import com.example.Shopzz.Entities.Cart;
import com.example.Shopzz.Entities.CartItems;
import com.example.Shopzz.Entities.Product;
import com.example.Shopzz.Repositories.CartItemsRepository;
import com.example.Shopzz.Repositories.CartRepository;
import com.example.Shopzz.Repositories.ProductRepository;
import com.example.Shopzz.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemsService {


    private final CartRepository cartRepository;
    private final CartItemsRepository cartItemsRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;


    //ADD CART ITEMS TO CART
    @Transactional
    public Cart addItemToCart(Integer cartId,Integer productId) {

        Cart cart=cartRepository.findById(cartId)
                .orElseThrow(()->new CartNotFoundException(cartId));

        Product product=productRepository.findById(productId)
                .orElseThrow(()->new ProductNotFoundException(productId));

        Optional<CartItems> item=cartItemsRepository
                .findByCart_CartIdAndProduct_ProductId(cartId,productId);



        if(item.isPresent()){
            CartItems cartItem=item.get();
            int newQty = cartItem.getQuantity() + 1;
            if(product.getStock()<newQty){
                throw new QuantityException(product.getStock());
            }
            cartItem.setQuantity(newQty);

        }else {
            if (product.getStock() < 1)
                throw new QuantityException(product.getStock());

            CartItems cartItem =new CartItems();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setPriceAtAddTime(product.getPrice());

            cart.addItem(cartItem);
        }

        return cartRepository.findCartWithItemsByCartId(cartId)
                .orElseThrow(()->new CartNotFoundException(cartId));
    }

    @Transactional
    public Cart increaseQuantity(Integer cartId,Integer productId){

        CartItems item=cartItemsRepository
                .findByCart_CartIdAndProduct_ProductId(cartId,productId)
                .orElseThrow(()->new CartItemNotFoundInCartException(cartId));


        int newQty = item.getQuantity() + 1;

        if (item.getProduct().getStock() < newQty) {
            throw new QuantityException(item.getProduct().getStock());
        }
        item.setQuantity(newQty);

        return cartRepository.findCartWithItemsByCartId(cartId)
                .orElseThrow(()->new CartNotFoundException(cartId));
    }

    @Transactional
    public Cart decreaseQuantity(Integer cartId,Integer productId){

        CartItems item=cartItemsRepository
                .findByCart_CartIdAndProduct_ProductId(cartId,productId)
                .orElseThrow(()->new CartItemNotFoundInCartException(cartId));

        int newQty = item.getQuantity() - 1;

        if (newQty<=0) {
            Cart cart=item.getCart();
            cart.removeItem(item);
        }else{
            item.setQuantity(newQty);
        }
        return cartRepository.findCartWithItemsByCartId(cartId)
                .orElseThrow(()->new CartNotFoundException(cartId));
    }

    //REMOVE ITEMS FROM CART
    @Transactional
    public Cart removeItemFromCart(Integer cartId, Integer productId) {

        //CHECK CART ID
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));

        //CHECK CART ITEM IS EXIST IN CART
        CartItems items=cartItemsRepository.findByCart_CartIdAndProduct_ProductId(cartId,productId)
                .orElseThrow(()->new CartItemNotFoundInCartException(cartId));

        cart.removeItem(items);

        return cartRepository.findCartWithItemsByCartId(cartId)
                .orElseThrow(()->new CartNotFoundException(cartId));
    }

    //CLEAR CART
    @Transactional
    public void clearCart(Integer cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));

        cart.getItems().clear();
    }
}
