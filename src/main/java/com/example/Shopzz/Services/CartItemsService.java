package com.example.Shopzz.Services;

import com.example.Shopzz.CustomExceptions.CartAndCartItems.*;
import com.example.Shopzz.CustomExceptions.Products.ProductNotFoundException;
import com.example.Shopzz.CustomExceptions.Users.UserNotFoundException;
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

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemsService {


    private final CartRepository cartRepository;
    private final CartItemsRepository cartItemsRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    //GET CART ITEMS BY USER ID
    public List<CartItems> getCartItemsByUserId(Integer userId){
        if(!userRepository.existsById(userId)){
            throw new UserNotFoundException(userId);
        }
        Cart cart=cartRepository.findByUserUserId(userId)
                .orElseThrow(()->new CartNotFoundForUserException(userId));

        List<CartItems> items=cartItemsRepository.findByCart(cart);
        return items;
    }

    //GET CART ITEMS BY CART ID
    public List<CartItems> getCartItemsByCartId(Integer cartId){
        Cart cart=cartRepository.findById(cartId)
                .orElseThrow(()->new CartNotFoundException(cartId));
        return cartItemsRepository.findByCart(cart);
    }

    //ADD CART ITEMS TO CART
    @Transactional
    public Cart addItemToCart(Integer cartId,Integer productId,Integer quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));

        Product product=productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Optional<CartItems> existingCart=cartItemsRepository.findByCartAndProduct(cart,product);


        if(existingCart.isPresent()){
            CartItems cartItem =existingCart.get();
            int newQty = cartItem.getQuantity() + quantity;
            if(product.getStock()<quantity){
                throw new QuantityException(product.getStock());
            }
            cartItem.setQuantity(newQty);
        }else {
            if (product.getStock() < quantity)
                throw new QuantityException(product.getStock());
            CartItems cartItem=new CartItems();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setPriceAtAddTime(product.getPrice());

            cart.addItem(cartItem);
        }
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart increaseQuantity(Integer cartId,Integer cartItemId,Integer quantity){

        if (quantity == null || quantity <= 0) {
            throw new QuantityException();
        }
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));

        CartItems item=cartItemsRepository.findById(cartItemId)
                .orElseThrow(()->new CartItemNotFoundException(cartItemId));

        if (!item.getCart().getCartId().equals(cartId)) {
            throw new CartItemNotFoundInCartException(cartId);
        }

        int newQty = item.getQuantity() + quantity;

        if (item.getProduct().getStock() < newQty) {
            throw new QuantityException(item.getProduct().getStock());
        }
        item.setQuantity(newQty);
        return cart;
    }

    //REMOVE ITEMS FROM CART
    public Cart removeItemFromCart(Integer cartId, Integer cartItemId) {

        //CHECK CART ID
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));

        //CHECK CART ITEM ID
        CartItems cartItem = cartItemsRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));

        // CHECK IF ITEM IS IN CART
        if (!cartItem.getCart().getCartId().equals(cartId)) {
            throw new CartItemNotFoundInCartException(cartId);
        }

        cart.removeItem(cartItem);
        return cartRepository.save(cart);
    }

    //CLEAR CART
    @Transactional
    public void clearCart(Integer cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));

        List<CartItems> items = cartItemsRepository.findByCart(cart);

        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
