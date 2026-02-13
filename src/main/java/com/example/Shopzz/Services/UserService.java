package com.example.Shopzz.Services;
import com.example.Shopzz.CustomExceptions.Users.UserEmailAlreadyExistsException;
import com.example.Shopzz.CustomExceptions.Users.UserNotFoundException;
import com.example.Shopzz.DTO.Mapper.UserMapper;
import com.example.Shopzz.DTO.PageMapper;
import com.example.Shopzz.DTO.Request.UserCreateRequest;
import com.example.Shopzz.DTO.Request.UserUpdateRequest;
import com.example.Shopzz.DTO.Response.PageResponse;
import com.example.Shopzz.DTO.Response.UserResponse;
import com.example.Shopzz.Enums.Role; import com.example.Shopzz.Entities.User;
import com.example.Shopzz.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    //GET ALL USERS
    public PageResponse<UserResponse> getAllUsers(Pageable pageable){
        Page<User> page=userRepository.findAll(pageable);
        return PageMapper.toPageResponse(page,UserMapper::response);
    }

    //GET USER BY USER ID
    public UserResponse getUserByUserId(Integer userId){
        User user=userRepository.findById(userId) .orElseThrow(()-> new UserNotFoundException(userId));
        return UserMapper.response(user);
    }
    //GET USER BY USERNAME
    public PageResponse<UserResponse> getUsersByUserName(String userName,Pageable pageable){
        Page<User> page=userRepository.findByUsername(userName,pageable);
        return PageMapper.toPageResponse(page,UserMapper::response);
    }

    // GET USER BY USER EMAIL
    public UserResponse getUserByUserEmail(String email){
        User user=userRepository.findByEmail(email) .orElseThrow(()->new UserNotFoundException(email));
        return UserMapper.response(user);
    }

    // FIND BY ROLE
    public PageResponse<UserResponse> getUsersByRole(Role role,Pageable pageable){
        Page<User> page=userRepository.findByRole(role,pageable);
        return PageMapper.toPageResponse(page,UserMapper::response);
    }

    // FIND BY ACTIVE
    public PageResponse<UserResponse> getUsersByActive(Boolean active,Pageable pageable){
        Page<User> page=userRepository.findByActive(active,pageable);
        return PageMapper.toPageResponse(page,UserMapper::response);
    }

    // ADD NEW USER
    public UserResponse addUser(UserCreateRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new UserEmailAlreadyExistsException(request.getEmail());
        }
        User user=UserMapper.create(request);
        User saved=userRepository.save(user);
        return UserMapper.response(saved);
    }
    // DELETE USER BY USER ID
    @Transactional
    public void deleteUser(Integer userId){
        User user=userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException(userId));
        user.setActive(false);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }
    // REACTIVATE USER IF DELETED
    @Transactional
    public void reactivateUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException(userId));

        if (Boolean.TRUE.equals(user.getActive())) {
            return;
        }
        user.setActive(true);
        user.setDeletedAt(null);
        userRepository.save(user);
    }
    // UPDATE USER DETAILS BY USER ID
    @Transactional
    public UserResponse updateUser(Integer userId, UserUpdateRequest request){
        return userRepository.findById(userId) .map(existingUser -> {
            if(request.getUsername()!=null){
                existingUser.setUsername(request.getUsername());
            } if(request.getEmail()!=null){
                if(userRepository.existsByEmailAndUserId(request.getEmail(), userId)){
                    throw new UserEmailAlreadyExistsException(request.getEmail());
                } existingUser.setEmail(request.getEmail());
            } if(request.getPassword()!=null){
                existingUser.setPassword(request.getPassword());
            } if(request.getRole()!=null){
                existingUser.setRole(request.getRole());
            } User user=userRepository.save(existingUser);
            return UserMapper.response(user);
        }) .orElseThrow(()->new UserNotFoundException(userId));
    }
}