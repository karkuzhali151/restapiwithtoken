package com.example.restapiwithtoken.repository;

import com.example.restapiwithtoken.user.Food;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Integer> {

    Optional<Food> findByFoodid(int foodid);

    List<Food> findByCreatedby(String createdby);

}
