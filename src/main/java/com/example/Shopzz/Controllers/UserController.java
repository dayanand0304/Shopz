package com.example.Shopzz.Controllers;

import com.example.Shopzz.DTO.LoginRequest;
import com.example.Shopzz.DTO.LoginResponse;
import com.example.Shopzz.Models.*;
import com.example.Shopzz.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemsService cartItemsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemsService orderItemsService;

    //REGISTER USER
    @PostMapping("/registerUser")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        User savedUser = userService.userRegister(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }


    //DELETE USER BY USER ID
    @DeleteMapping("/deleteUser/userId/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable Integer userId){
        String message= userService.deleteUserByUserId(userId);
        if(message.contains("doesn't exists")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(message);
        }else{
            return ResponseEntity.ok(message);
        }
    }


    //UPDATE USER BY USER ID
    @PutMapping("/updateUser/userId/{userId}")
    public ResponseEntity<?> updateUserById(@RequestBody User user, @PathVariable Integer userId) {
        User updatedUser = userService.updateUserByUserId(userId,user);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with Id " + userId + " not found");
        }
    }

    //GET PRODUCTS
    @GetMapping("/getProducts")
    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

    //GET PRODUCT BY PRODUCT ID
    @GetMapping("/getProduct/productId/{productId}")
    public ResponseEntity<?> getById(@PathVariable Integer productId){
        Optional<Product> product=productService.getProductByProductId(productId);
        if(product.isPresent()){
            return ResponseEntity.ok(product.get());
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("There is No Product With ID No.: "+productId);
        }
    }

    //GET PRODUCT BY PRODUCT CATEGORY
    @GetMapping("/getProduct/category/{category}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable String category){
        List<Product> products=productService.getProductsByCategory(category);
        if(products.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("There is No Products With This Category...");
        }else{
            return ResponseEntity.ok(products);
        }
    }

    //GET PRODUCT BY PRODUCT NAME
    @GetMapping("getProduct/productName/{productName}")
    public ResponseEntity<?> getProductsByName(@PathVariable String productName){
        Optional<Product> products=productService.getProductByProductName(productName);
        if(products.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("There is No Products With This Product Name...");
        }else{
            return ResponseEntity.ok(products);
        }
    }

    //GET ORDER BY USER ID
    @GetMapping("/getOrder/userId/{userId}")
    public ResponseEntity<?> getOrdersByUserId(@PathVariable Integer userId) {
        try {
            List<Order> orders = orderService.getOrdersByUserId(userId);
            if (orders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No orders found for user ID: " + userId);
            }
            return ResponseEntity.ok(orders);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    //PLACE ORDER
    @PostMapping("/placeOrder/userId/{userId}")
    public ResponseEntity<?> placeOrder(@PathVariable Integer userId) {
        try {
            Order order = orderService.placeOrder(userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error placing order: " + e.getMessage());
        }
    }

    //CANCEL ORDER
    @PutMapping("/cancelOrder/orderId/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable Integer orderId) {
        try {
            String message = orderService.cancelOrder(orderId);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //GET ORDER ITEMS BY ORDER ID
    @GetMapping("/details/orderId/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable Integer orderId) {
        try {
            List<OrderItems> orderItems = orderItemsService.getItemsByOrderId(orderId);
            if (orderItems.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No items found for order ID: " + orderId);
            }
            return ResponseEntity.ok(orderItems);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    //ADD ITEMS TO CART
    @PostMapping("/addItems/cartId/{cartId}/productId/{productId}/{quantity}")
    public ResponseEntity<String> addItemToCart(@PathVariable Integer cartId,
                                                @PathVariable Integer productId,
                                                @PathVariable Integer quantity) {
        cartItemsService.addItemToCart(cartId,productId,quantity);
        return ResponseEntity.ok("Product added to cart successfully");
    }

    // GET CART BY USER ID
    @GetMapping("/getCart/userId/{userId}")
    public ResponseEntity<?> getCartByUserId(@PathVariable Integer userId) {
        Cart cart = cartService.getCartsByUserId(userId);
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            return ResponseEntity.ok("Cart is empty!");
        }
        return ResponseEntity.ok(cart);
    }

    // GET CART ITEMS BY USER ID
    @GetMapping("/getItems/userId/{userId}")
    public ResponseEntity<List<CartItems>> getCartItems(@PathVariable Integer userId) {
        List<CartItems> items = cartItemsService.getCartItemsByUserId(userId);
        return ResponseEntity.ok(items);
    }

    //REMOVE ITEM FROM CART
    @DeleteMapping("/removeItem/userId/{userId}/productId/{productId}")
    public ResponseEntity<String> removeItemFromCart(@PathVariable Integer userId,
                                                     @PathVariable Integer productId) {
        cartItemsService.removeItemFromCart(userId, productId);
        return ResponseEntity.ok("Item removed from cart");
    }

}
