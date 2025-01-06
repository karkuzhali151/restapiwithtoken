package com.example.restapiwithtoken.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.example.restapiwithtoken.repository.FoodRepository;
import com.example.restapiwithtoken.request.FoodRequest;
import com.example.restapiwithtoken.response.AuthenticationResponse;
import com.example.restapiwithtoken.response.FoodResponse;
import com.example.restapiwithtoken.user.Food;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;


    public AuthenticationResponse savefood(FoodRequest foodRequest) {

       var food = Food.builder()
                .foodname(foodRequest.getFoodname())
                .fooddecription(foodRequest.getFooddecription())
                .createdby(foodRequest.getEmployeeid())
                .createdat(new Date(System.currentTimeMillis()))
                .build();
                
                foodRepository.save(food);

        return AuthenticationResponse.builder()
        .message("Food Added successfully")
        .status("1")
        .build();
    }

    public FoodResponse getfoodbyempid(String empid) {

        var food = foodRepository.findByCreatedby(empid);

        if(food.isEmpty()) {
            return FoodResponse.builder()
            .message("No food found")
            .status("0")
            .build();
        }

        ObjectMapper mapper = new ObjectMapper();

        ArrayNode roles = mapper.createArrayNode();
        for(Food f : food) {
            ObjectNode foodNode = mapper.createObjectNode();
            foodNode.put("foodid", f.getFoodid());
            foodNode.put("foodname", f.getFoodname());
            foodNode.put("fooddecription", f.getFooddecription());
            foodNode.put("createdby", f.getCreatedby());
            foodNode.put("createdat", f.getCreatedat().toString());

            roles.add(foodNode);
        }

         return FoodResponse.builder()
         .message("Data Loaded successfully")
         .status("1")
         .data(roles)
         .build();
     }

     public FoodResponse getfoodbyid(int id) {

        var foodOptional = foodRepository.findByFoodid(id);

        if(foodOptional.isEmpty()) {
            return FoodResponse.builder()
            .message("No food found")
            .status("0")
            .build();
        }

        Food food = foodOptional.get();
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode data = mapper.createObjectNode();

        data.put("foodid", food.getFoodid());
        data.put("foodname", food.getFoodname());
        data.put("fooddecription", food.getFooddecription());
        data.put("createdby", food.getCreatedby());
        data.put("createdat", food.getCreatedat().toString());
          

         return FoodResponse.builder()
         .message("Data Loaded successfully")
         .status("1")
         .data(data)
         .build();
     }

}
