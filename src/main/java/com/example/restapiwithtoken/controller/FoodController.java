package com.example.restapiwithtoken.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.restapiwithtoken.request.FoodRequest;
import com.example.restapiwithtoken.response.AuthenticationResponse;
import com.example.restapiwithtoken.response.EmployeeDataResponse;
import com.example.restapiwithtoken.response.FoodResponse;
import com.example.restapiwithtoken.service.FoodService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/food/")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    private static final String UPLOAD_DIR = "uploads/";

     // Ensure the upload directory exists
     static {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

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

    @PostMapping("uploadimage")
    public ResponseEntity<EmployeeDataResponse> uploadimage(@RequestParam("file") MultipartFile file) {
        String fileName = "";
        try {

            fileName = System.currentTimeMillis()+".jpg";;
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Files.write(filePath, file.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(foodService.saveImage(fileName));
    }

    @PostMapping("updatefood")
    public ResponseEntity<AuthenticationResponse> updatefood(@RequestBody FoodRequest foodRequest) {
        
        return ResponseEntity.ok(foodService.updatefood(foodRequest));
    }

}
