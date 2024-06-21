package com.jwt.rolebasedjwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jwt.rolebasedjwt.entities.Role;
import com.jwt.rolebasedjwt.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{

    Optional<User> findByEmail(String username);
    User findByRole(Role role);
    
}
