package com.example.Shopzz.Controllers;

import com.example.Shopzz.Models.Order;
import com.example.Shopzz.Models.Product;
import com.example.Shopzz.Models.User;
import com.example.Shopzz.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

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

    //GET ALL USERS
    @GetMapping("/getAll-users")
    public List<User> getUsers(){
        return userService.getAllUsers();
    }


    //GET USER BY USER ID
    @GetMapping("/getUser/id/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Integer userId) {
        Optional<User> users=userService.getUserByUserId(userId);
        if(users.isPresent()){
            return ResponseEntity.ok(users.get());
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User id With "+ userId +" soesn't Exists");
        }
    }

    //GET USER BY USERNAME
    @GetMapping("/getUser/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username){
        Optional<User> user=userService.getUserByUserName(username);
        if(user.isPresent()){
            return ResponseEntity.ok(user.get());
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Username with "+username+" doesn't Exists");
        }
    }


    //GET USER BY EMAIL
    @GetMapping("/getUser/mail/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email){
        Optional<User> user=userService.getUserByUserEmail(email);
        if(user.isPresent()){
            return ResponseEntity.ok(user.get());
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User With email "+email+" Not Found");
        }
    }


    //REGISTER ADMIN
    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody User user) {
        User savedAdmin = userService.adminRegister(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAdmin);
    }


    //ADD PRODUCT
    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.addProduct(product));
    }


    //DELETE PRODUCT BY PRODUCT ID
    @DeleteMapping("/deleteProduct/productId/{productId}")
    public ResponseEntity<String> deleteProductByProductId(@PathVariable Integer productId){
        String message=productService.deleteProductByProductId(productId);
        if(message.contains("doesn't exists")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(message);
        }else{
            return ResponseEntity.ok(message);
        }
    }


    //UPDATE PRODUCT BY PRODUCT ID
    @PutMapping("/updateProduct/productId/{productId}")
    public ResponseEntity<?> updateProduct(
            @RequestBody Product product,
            @PathVariable Integer productId) {
        Product updatedProduct = productService.updateProductByProductId(productId,product);
        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product with ID " + productId + " not found");
        }
    }

    //GET ALL ORDERS
    @GetMapping("/getAll-orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }


    //GET ORDERS BY STATUS
    @GetMapping("/getOrder/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(String status){
        List<Order> orders=orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    //UPDATE STATUS OF ORDER
    @PutMapping("updateStatus/orderId/{orderId}/status/{status}")
    public ResponseEntity<Order> updateStatus(@PathVariable Integer orderId,
                                              @PathVariable String status){
        Order order=orderService.updateOrderStatus(orderId,status);
        return ResponseEntity.ok(order);
    }
}
