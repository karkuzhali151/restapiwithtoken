package com.example.restapiwithtoken.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restapiwithtoken.user.User;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Integer> {
    
    Optional<User> findByEmail(String email);

    List<User> findByEmployeeid(int employeeid);


}
