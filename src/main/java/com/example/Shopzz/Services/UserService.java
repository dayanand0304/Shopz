package com.example.Shopzz.Services;

import com.example.Shopzz.DTO.LoginResponse;
import com.example.Shopzz.Models.User;
import com.example.Shopzz.Repositries.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    //GET ALL USERS
    public List<User> getAllUsers(){
        List<User> users=userRepository.findAll(Sort.by("username"));
        users.forEach(u-> u.setPassword(null));
        return users;
    }


    //GET USER BY USER ID
    public Optional<User> getUserByUserId(Integer userId){
        return userRepository.findById(userId)
                .map(u->{
                    u.setPassword(null);
                    return u;
                });
    }

    //GET USER BY USERNAME
    public Optional<User> getUserByUserName(String userName){
        return userRepository.findByUsername(userName)
                .map(u -> {
                    u.setPassword(null);
                    return u;
                });
    }

    //GET USER BY USER EMAIL
    public Optional<User> getUserByUserEmail(String email){
        return userRepository.findByEmail(email)
                .map(u -> {
                    u.setPassword(null);
                    return u;
                });
    }

    private static final PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    //REGISTER NEW USER
    public User userRegister(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        User saved=userRepository.save(user);
        saved.setPassword(null);
        return saved;
    }

    //REGISTER NEW ADMIN
    public User adminRegister(User admin){
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setRole("ADMIN");
        User saved= userRepository.save(admin);
        saved.setPassword(null);
        return saved;
    }

    //DELETE USER BY USER ID
    public String deleteUserByUserId(Integer userId){
        if(userRepository.existsById(userId)){
            userRepository.deleteById(userId);
            return "User : "+ userId+" Deleted Successfully";
        }else{
            return "There is no User with "+userId;
        }
    }

    //UPDATE USER DETAILS BY USER ID
    public User updateUserByUserId(Integer userId, User updatedUser){
        return userRepository.findById(userId)
                .map(existingUser -> {
                    if(updatedUser.getUsername()!=null){
                        existingUser.setUsername(updatedUser.getUsername());
                    }
                    if(updatedUser.getEmail()!=null){
                        existingUser.setEmail(updatedUser.getEmail());
                    }
                    if(updatedUser.getPassword()!=null){
                        existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                    }
                    if(updatedUser.getRole()!=null){
                        existingUser.setRole(updatedUser.getRole());
                    }
                    User saved=userRepository.save(existingUser);
                    saved.setPassword(null);
                    return saved;
                })
                .orElse(null);
    }

    // LOGIN BY USERNAME
    public Optional<LoginResponse> login(String username, String rawPassword) {
        return userRepository.findByUsername(username)
                .filter(u -> passwordEncoder.matches(rawPassword, u.getPassword()))
                .map(u -> new LoginResponse(u.getUserId(), u.getUsername(), u.getEmail(), u.getRole()));
    }

    // LOGIN BY EMAIL
    public Optional<LoginResponse> loginByEmail(String email, String rawPassword) {
        return userRepository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(rawPassword, u.getPassword()))
                .map(u -> new LoginResponse(u.getUserId(), u.getUsername(), u.getEmail(), u.getRole()));
    }
}
