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

import com.example.restapiwithtoken.request.UpdateRequest;
import com.example.restapiwithtoken.response.AuthenticationResponse;
import com.example.restapiwithtoken.response.EmployeeDataResponse;
import com.example.restapiwithtoken.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employee/")
@RequiredArgsConstructor
public class EmployeeController {


    private final AuthenticationService authenticationService;
    
    
    private static final String UPLOAD_DIR = "uploads/";

     // Ensure the upload directory exists
     static {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }


    @GetMapping("GetEmployee")
    public ResponseEntity<EmployeeDataResponse> getemployee() {

        return ResponseEntity.ok(authenticationService.getemployeebyid());
    }

    @PostMapping("UpdateEmployee")
    public ResponseEntity<AuthenticationResponse> updateemployee(@RequestBody UpdateRequest updateRequest) {
    
        return ResponseEntity.ok(authenticationService.updateemployee(updateRequest));
    }

    @PostMapping("uploadimage")
    public ResponseEntity<EmployeeDataResponse> uploadimage(@RequestParam("file") MultipartFile file) {
        String fileName = "";
        try {

            fileName = ""+System.currentTimeMillis();
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Files.write(filePath, file.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(authenticationService.saveImage(fileName));
    }
}
