package com.example.restapiwithtoken.service;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.restapiwithtoken.repository.UserRepository;
import com.example.restapiwithtoken.request.LoginRequest;
import com.example.restapiwithtoken.request.RegisterRequest;
import com.example.restapiwithtoken.request.UpdateRequest;
import com.example.restapiwithtoken.response.AuthenticationResponse;
import com.example.restapiwithtoken.response.EmployeeDataResponse;
import com.example.restapiwithtoken.response.LoginResponse;
import com.example.restapiwithtoken.user.Role;
import com.example.restapiwithtoken.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JWTService jwtService;

    private final AuthenticationManager authenticationManager;
    
    public AuthenticationResponse register(RegisterRequest registerRequest) {

        var users = userRepository.findByEmail(registerRequest.getEmail());
        if (users.isPresent()) {
          return AuthenticationResponse.builder()
                .message("User already exists")
                .status("0")
                .build();
        
        }
        var user = User.builder()
                .employeename(registerRequest.getEmployeename())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();
                
        userRepository.save(user);
            
        return AuthenticationResponse.builder()
        .message("Employee registered successfully")
        .status("1")
        .build();
    }

    public LoginResponse login(LoginRequest loginRequest) {

        var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();

        if (user != null) {
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()) || !user.getEmail().equals(loginRequest.getEmail())) {
                return LoginResponse.builder()
                .message("Invalid Credentials")
                .status("0")
                .data(null)
                .build();
            
            }
        }else{
            return LoginResponse.builder()
            .message("User Not Found")
            .status("0")
            .data(null)
            .build();
        }
                       
    
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), 
                loginRequest.getPassword()));
            
        String jwttoken = jwtService.generateToken(user);

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode data = mapper.createObjectNode();
        data.put("AccessToken", jwttoken);
        data.put("TokenType","Bearer");
        data.put("employeeid", user.getEmployeeid());
        data.put("employeename", user.getEmployeename());
        data.put("email", user.getEmail());
        data.put("profileimage", user.getProfileimage());


        return LoginResponse.builder()
        .message("Login successfully")
        .status("1")
        .data(data)
        .build();
    }

    public EmployeeDataResponse getemployee() {

        // Using Access Token to get the Employee Details//
        var authentication = SecurityContextHolder.getContext().getAuthentication();
    
        var user =authentication.getPrincipal();

        User user1 = (User) user;
   
        var users = userRepository.findByEmail(user1.getEmail());
        if (users.isEmpty()) {
            return EmployeeDataResponse.builder()
            .message("User Not Found")
            .status("1")
            .build();
        }
        User existingUser = users.get();
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode data = mapper.createObjectNode();
        
            data.put("employeeid", existingUser.getEmployeeid());
            data.put("employeename", existingUser.getEmployeename());
            data.put("email", existingUser.getEmail());
            data.put("profileImage", existingUser.getProfileimage());

        return EmployeeDataResponse.builder()
        .message("Data Loaded successfully")
        .status("1")
        .data(data)
        .build();
    }

    public AuthenticationResponse updateemployee(UpdateRequest updateRequest) {

        var authentication = SecurityContextHolder.getContext().getAuthentication();
    
        var user =authentication.getPrincipal();

        User user1 = (User) user;
   
        var users = userRepository.findByEmail(user1.getEmail());
        if (users.isEmpty()) {
            return AuthenticationResponse.builder()
            .message("User not found")
            .status("1")
            .build();
        }

        User existingUser = users.get();
        existingUser.setEmployeename(updateRequest.getEmployeename());
        existingUser.setEmail(updateRequest.getEmail());

        userRepository.save(existingUser);

        return AuthenticationResponse.builder()
        .message("Data Updated successfully")
        .status("1")
        .build();
    }


    public EmployeeDataResponse saveImage(String filepath) {

        var authentication = SecurityContextHolder.getContext().getAuthentication();
    
        var user =authentication.getPrincipal();

        User user1 = (User) user;
   
        var users = userRepository.findByEmail(user1.getEmail());
        if (users.isEmpty()) {
            return EmployeeDataResponse.builder()
            .message("Image Upload failed")
            .status("1")
            .build();
        }
        User existingUser = users.get();
        String imageurl = "http://localhost:8080/api/image/"+filepath;
        existingUser.setProfileimage(imageurl);

        userRepository.save(existingUser);
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode data = mapper.createObjectNode();
            data.put("Imageurl", imageurl);
    
        return EmployeeDataResponse.builder()
        .message("Image Uploaded successfully")
        .status("1")
        .data(data)
        .build();
    }
}
