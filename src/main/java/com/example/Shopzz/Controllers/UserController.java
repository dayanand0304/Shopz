package com.example.Shopzz.Controllers;
import com.example.Shopzz.DTO.Request.UserCreateRequest;
import com.example.Shopzz.DTO.Request.UserUpdateRequest;
import com.example.Shopzz.DTO.Response.PageResponse;
import com.example.Shopzz.DTO.Response.UserResponse;
import com.example.Shopzz.Enums.Role;
import com.example.Shopzz.Repositories.UserRepository;
import com.example.Shopzz.Services.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    //GET ALL USERS
    @GetMapping
    public ResponseEntity<PageResponse<UserResponse>> getAllUsers(@RequestParam(required = false)String userName,
                                                                  @RequestParam(required = false)Role role,
                                                                  @RequestParam(required = false)Boolean active,
                                                                  @PageableDefault(size = 5)Pageable pageable ){

        PageResponse<UserResponse> users;
        if(userName!=null){
            users=userService.getUsersByUserName(userName,pageable);
        }else if(role!=null){
            users=userService.getUsersByRole(role,pageable);
        }else if(active!=null){
            users=userService.getUsersByActive(active, pageable);
        }else{
            users= userService.getAllUsers(pageable);
        }
        return ResponseEntity.ok(users);
    }

    //GET USER BY USER ID
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Integer userId) {
        return ResponseEntity.ok(userService.getUserByUserId(userId));
    }

    //GET USER BY EMAIL
    @GetMapping("/mail")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam
                                                           @Email(message = "Invalid email format")
                                                           @NotBlank(message = "email must not be blank")
                                                           String email) {

        return ResponseEntity.ok(userService.getUserByUserEmail(email));
    }

    @PatchMapping("/{userId}/reactivate")
    public ResponseEntity<Void> reactivateUser(@PathVariable Integer userId){
        userService.reactivateUser(userId);
        return ResponseEntity.noContent().build();
    }

    //ADD USER
    @PostMapping
    public ResponseEntity<UserResponse> addUser(@Valid @RequestBody UserCreateRequest request) {
        return ResponseEntity.ok(userService.addUser(request));
    }


    //DELETE USER BY USER ID
    @DeleteMapping("/{userId}/deactivate")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    //UPDATE USER BY USER ID
    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Integer userId,
                                            @Valid @RequestBody UserUpdateRequest request) {
       return ResponseEntity.ok(userService.updateUser(userId,request));
    }
}