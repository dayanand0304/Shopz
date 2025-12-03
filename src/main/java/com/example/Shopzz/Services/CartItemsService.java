package com.example.Shopzz.Services;

import com.example.Shopzz.Models.Cart;
import com.example.Shopzz.Models.CartItems;
import com.example.Shopzz.Models.Product;
import com.example.Shopzz.Models.User;
import com.example.Shopzz.Repositries.CartItemsRepository;
import com.example.Shopzz.Repositries.CartRepository;
import com.example.Shopzz.Repositries.ProductRepository;
import com.example.Shopzz.Repositries.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemsService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemsRepository cartItemsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    private final Logger log=LoggerFactory.getLogger(CartItemsService.class);

    //GET CART ITEMS BY USER ID
    public List<CartItems> getCartItemsByUserId(Integer userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        Cart cart=cartRepository.findByUser(user);
        if(cart==null)throw new ResourceNotFoundException("Cart not Found for this User");
        return cartItemsRepository.findByCart(cart);
    }

    //ADD CART ITEMS TO CART
    @Transactional
    public Cart addItemToCart(Integer cartId,Integer productId,Integer quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart with id " + cartId + " not found"));

        Product product=productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + productId + " not found"));


        if (product.getStock() != null && product.getStock() < quantity) {
            throw new IllegalArgumentException("Product have only quantity of:  " + product.getStock());
        }

        Optional<CartItems> existingCart=cartItemsRepository.findByCartAndProduct(cart,product);

        CartItems cartItem;

        if(existingCart.isPresent()){
            cartItem =existingCart.get();
            int newQty = cartItem.getQuantity() + quantity;
            cartItem.setQuantity(newQty);
            cartItemsRepository.save(cartItem);
            log.info("Updated Existing cartItem id:{} qty:{} in cart:id{}",cartItem.getCartItemId(),newQty,cart.getCartId());
        }else{
            cartItem=new CartItems();
            cartItem.setCart(cart);
            cartItem.setQuantity(quantity);
            cartItem.setProduct(product);
            CartItems saved=cartItemsRepository.save(cartItem);

            cart.getItems().add(saved);
            log.info("Added new cartItem id:{} productId:{} qty:{} to cart id:{}", saved.getCartItemId(), productId, quantity, cart.getCartId());
        }
        return cartRepository.save(cart);
    }

    //REMOVE ITEMS FROM CART
    public Cart removeItemFromCart(Integer cartId, Integer cartItemId) {

        //CHECK CART ID
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart with id " + cartId + " not found"));

        //CHECK CART ITEM ID
        CartItems cartItem = cartItemsRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem with id " + cartItemId + " not found"));

        // CHECK IF ITEM IS IN CART
        if (!cartItem.getCart().getCartId().equals(cart.getCartId())) {
            throw new IllegalArgumentException("CartItem does not belong to the specified cart");
        }

        // REMOVE ITEM FROM CART
        cart.getItems().removeIf(item -> item.getCartItemId().equals(cartItemId));

        // DELETE ITEM FROM DATABASE
        cartItemsRepository.delete(cartItem);
        log.info("Deleted cartItem id:{} from cart id:{}", cartItemId, cartId);

        return cartRepository.save(cart);

    }

    //CLEAR CART
    @Transactional
    public void clearCart(Integer cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart with id " + cartId + " not found"));

        //DELETE ITEMS FROM DATABASE
        List<CartItems> items = cartItemsRepository.findByCart(cart);
        if (!items.isEmpty()) {
            cartItemsRepository.deleteAll(items); // cascades if needed
        }

        //CLEAR MEMORY OBJECT
        cart.getItems().clear();
        cartRepository.save(cart);
        log.info("Cleared cart id:{}", cartId);
    }
}
