package com.example.Shopzz.Repositories;

import com.example.Shopzz.Enums.Role;
import com.example.Shopzz.Entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    Page<User> findByUsername(String username, Pageable pageable);

    Optional<User> findByEmail(String email);

    Page<User> findByRole(Role role, Pageable pageable);

    Page<User> findByActive(Boolean active, Pageable pageable);

    boolean existsByEmail(String email);

    boolean existsByEmailAndUserId(String email,Integer userId);
}
