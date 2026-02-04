package com.example.Shopzz.Controllers;
import com.example.Shopzz.CustomExceptions.Users.UserNotFoundException;
import com.example.Shopzz.DTO.Request.UserCreateRequest;
import com.example.Shopzz.DTO.Request.UserUpdateRequest;
import com.example.Shopzz.DTO.Response.UserResponse;
import com.example.Shopzz.DTO.Mapper.UserMapper;
import com.example.Shopzz.Enums.Role;
import com.example.Shopzz.Entities.*;
import com.example.Shopzz.Repositories.UserRepository;
import com.example.Shopzz.Services.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    //GET ALL USERS
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers()
                .stream()
                .map(UserMapper::response)
                .toList();
        return ResponseEntity.ok(users);
    }

    //GET USER BY USER ID
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Integer userId) {
        User user = userService.getUserByUserId(userId);
        return ResponseEntity.ok(UserMapper.response(user));
    }

    //GET USER BY USERNAME
    @GetMapping("/username/{username}")
    public ResponseEntity<List<UserResponse>> getUsersByUsername(@PathVariable String username) {
        List<UserResponse> user = userService.getUsersByUserName(username)
                .stream()
                .map(UserMapper::response)
                .toList();
        return ResponseEntity.ok(user);
    }

    //GET USER BY EMAIL
    @GetMapping("/mail")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam
                                                           @Email(message = "Invalid email format")
                                                           @NotBlank(message = "email must not be blank")
                                                           String email) {
        User user = userService.getUserByUserEmail(email);
        return ResponseEntity.ok(UserMapper.response(user));
    }

    //GET USER BY ROLE
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserResponse>> getUserByRole(@PathVariable Role role) {
        List<UserResponse> user = userService.getUsersByRole(role)
                .stream()
                .map(UserMapper::response)
                .toList();
        return ResponseEntity.ok(user);
    }

    //GET USER BY ACTIVE
    @GetMapping("/active")
    public ResponseEntity<List<UserResponse>> getUserByRole(@RequestParam Boolean active) {
        List<UserResponse> user = userService.getUsersByActive(active)
                .stream()
                .map(UserMapper::response)
                .toList();
        return ResponseEntity.ok(user);
    }


    @PatchMapping("/reactivate/{userId}")
    public ResponseEntity<Void> reactivateUser(@PathVariable Integer userId){
        userService.reactivateUser(userId);
        return ResponseEntity.noContent().build();
    }

    //ADD USER
    @PostMapping
    public ResponseEntity<UserResponse> addUser(@Valid @RequestBody UserCreateRequest request) {
        User newUser = UserMapper.create(request);
        User saved = userService.addUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.response(saved));
    }


    //DELETE USER BY USER ID
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    //UPDATE USER BY USER ID
    @PatchMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Integer userId,
                                            @Valid @RequestBody UserUpdateRequest request) {
        User user =userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException(userId));
        UserMapper.update(user,request);
        User updated = userService.updateUser(userId, user);
        return ResponseEntity.ok(UserMapper.response(updated));
    }
}