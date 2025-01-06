package com.example.restapiwithtoken.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.restapiwithtoken.request.FoodRequest;
import com.example.restapiwithtoken.response.AuthenticationResponse;
import com.example.restapiwithtoken.response.FoodResponse;
import com.example.restapiwithtoken.service.FoodService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/food/")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @PostMapping("addfood")
    public ResponseEntity<AuthenticationResponse> addfood(@RequestBody FoodRequest foodRequest) {
        
        return ResponseEntity.ok(foodService.savefood(foodRequest));
    }

    @GetMapping("getfoodbyempid")
    public ResponseEntity<FoodResponse> getfoodbyempid(@RequestParam String id) {
        
        return ResponseEntity.ok(foodService.getfoodbyempid(id));
    }

    @GetMapping("getfoodbyid")
    public ResponseEntity<FoodResponse> getfoodbyid(@RequestParam int id) {
        
        return ResponseEntity.ok(foodService.getfoodbyid(id));
    }

}
