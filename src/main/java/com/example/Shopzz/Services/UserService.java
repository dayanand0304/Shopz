package com.example.Shopzz.Services;

import com.example.Shopzz.CustomExceptions.Users.UserEmailAlreadyExistsException;
import com.example.Shopzz.CustomExceptions.Users.UserNotFoundException;
import com.example.Shopzz.Enums.Role;
import com.example.Shopzz.Entities.User;
import com.example.Shopzz.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    //GET ALL USERS
    public List<User> getAllUsers(){
        return userRepository.findAll(Sort.by(("username")));
    }


    //GET USER BY USER ID
    public User getUserByUserId(Integer userId){
        return userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(userId));
    }

    //GET USER BY USERNAME
    public List<User> getUsersByUserName(String userName){
        List<User> users=userRepository.findByUsername(userName);
        if(users.isEmpty()){
            throw new UserNotFoundException(userName);
        }
        return users;
    }

    //GET USER BY USER EMAIL
    public User getUserByUserEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException(email));
    }

    //FIND BY ROLE
    public List<User> getUsersByRole(Role role){
        List<User> users=userRepository.findByRole(role);
        if(users.isEmpty()){
            throw new UserNotFoundException(role);
        }
        return users;
    }

    //ADD NEW USER
    public User addUser(User user){
        if(userRepository.existsByEmail(user.getEmail())){
            throw new UserEmailAlreadyExistsException(user.getEmail());
        }
        return userRepository.save(user);
    }

    //DELETE USER BY USER ID
    @Transactional
    public void deleteUser(Integer userId){
        User user=userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException(userId));

        userRepository.delete(user);
    }

    //UPDATE USER DETAILS BY USER ID
    @Transactional
    public User updateUser(Integer userId, User updatedUser){
        return userRepository.findById(userId)
                .map(existingUser -> {
                    if(updatedUser.getUsername()!=null){
                        existingUser.setUsername(updatedUser.getUsername());
                    }
                    if(updatedUser.getEmail()!=null){
                        if(userRepository.existsByEmailAndUserId(updatedUser.getEmail(), userId)){
                            throw new UserEmailAlreadyExistsException(updatedUser.getEmail());
                        }
                        existingUser.setEmail(updatedUser.getEmail());
                    }
                    if(updatedUser.getPassword()!=null){
                        existingUser.setPassword(updatedUser.getPassword());
                    }
                    if(updatedUser.getRole()!=null){
                        existingUser.setRole(updatedUser.getRole());
                    }
                    return userRepository.save(existingUser);
                })
                .orElseThrow(()->new UserNotFoundException(userId));
    }
}
