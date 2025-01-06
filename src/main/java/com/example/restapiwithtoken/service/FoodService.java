package com.example.restapiwithtoken.service;

import java.util.Date;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.restapiwithtoken.repository.FoodRepository;
import com.example.restapiwithtoken.repository.UserRepository;
import com.example.restapiwithtoken.request.FoodRequest;
import com.example.restapiwithtoken.response.AuthenticationResponse;
import com.example.restapiwithtoken.response.EmployeeDataResponse;
import com.example.restapiwithtoken.response.FoodResponse;
import com.example.restapiwithtoken.user.Food;
import com.example.restapiwithtoken.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;

    private final UserRepository userRepository;


    public AuthenticationResponse savefood(FoodRequest foodRequest) {

       var food = Food.builder()
                .foodname(foodRequest.getFoodname())
                .fooddecription(foodRequest.getFooddecription())
                .foodimage(foodRequest.getFoodimage())
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
            foodNode.put("foodimage", f.getFoodimage());
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


     public EmployeeDataResponse saveImage(String filepath) {

        String imageurl = "http://localhost:8080/api/image/"+filepath;
       
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode data = mapper.createObjectNode();
            data.put("Imageurl", imageurl);
    
        return EmployeeDataResponse.builder()
        .message("Image Uploaded successfully")
        .status("1")
        .data(data)
        .build();
    }


    public AuthenticationResponse updatefood(FoodRequest foodRequest) {


        var authentication = SecurityContextHolder.getContext().getAuthentication();
    
        var user =authentication.getPrincipal();

        User user1 = (User) user;
   
        var users = userRepository.findByEmail(user1.getEmail());
    
        var food = foodRepository.findByFoodid(foodRequest.getFoodid());
        if (food.isEmpty()) {
            return AuthenticationResponse.builder()
            .message("Food not found")
            .status("1")
            .build();
        }

        var empid = users.get().getEmployeeid();
        var createdby = Integer.parseInt(food.get().getCreatedby());

        if (empid != createdby) {
            return AuthenticationResponse.builder()
            .message("User not found")
            .status("1")
            .build();
        }

        Food existingUser = food.get();
            existingUser.setFooddecription(foodRequest.getFooddecription());
            existingUser.setFoodimage(foodRequest.getFoodimage());
            existingUser.setFoodname(foodRequest.getFoodname());
            
            foodRepository.save(existingUser);
 
        return AuthenticationResponse.builder()
            .message("Food Updated successfully")
            .status("1")
            .build();
     }

}
