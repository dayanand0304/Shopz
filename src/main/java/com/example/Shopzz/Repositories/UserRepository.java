package com.example.Shopzz.Repositories;

import com.example.Shopzz.Enums.Role;
import com.example.Shopzz.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    List<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);

    List<User> findByActive(Boolean active);

    boolean existsByEmail(String email);

    boolean existsByEmailAndUserId(String email,Integer userId);
}
