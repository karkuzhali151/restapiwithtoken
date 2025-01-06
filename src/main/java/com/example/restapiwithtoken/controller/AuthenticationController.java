package com.example.restapiwithtoken.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restapiwithtoken.request.LoginRequest;
import com.example.restapiwithtoken.request.RegisterRequest;
import com.example.restapiwithtoken.response.AuthenticationResponse;
import com.example.restapiwithtoken.response.LoginResponse;
import com.example.restapiwithtoken.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class AuthenticationController {

    private static final String UPLOAD_DIR = "uploads/";
    private final AuthenticationService authenticationService;




    @PostMapping("register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest) {
    
        return ResponseEntity.ok(authenticationService.register(registerRequest));
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> register(@RequestBody LoginRequest loginRequest) {
    
        return ResponseEntity.ok(authenticationService.login(loginRequest));
    }

     @GetMapping("test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Endpoint is accessible");
    }

     @GetMapping("image/{imageName}")
    public ResponseEntity<Resource> getImages(@PathVariable String imageName) {
        try {
            Path imagePath = Paths.get(UPLOAD_DIR).resolve(imageName).normalize();
            Resource resource = new UrlResource(imagePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = Files.probeContentType(imagePath);
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imageName + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    

}
