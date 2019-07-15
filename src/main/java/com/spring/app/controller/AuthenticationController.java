package com.spring.app.controller;

import java.util.Collections;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.app.model.CustomResponse;
import com.spring.app.model.RoleModel;
import com.spring.app.model.RoleModel.RoleName;
import com.spring.app.model.UserModel;
import com.spring.app.payload.authentication.LoginRequest;
import com.spring.app.payload.authentication.RegisterRequest;
import com.spring.app.repository.RoleRepository;
import com.spring.app.repository.UserRepository;
import com.spring.app.security.jwt.JwtTokenProvider;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    RoleRepository roleRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;
    
    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    
    /**
     * Register handler
     * @param registerRequest
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<CustomResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if(userRepository.existsByUsername(registerRequest.getUsername())) {
            CustomResponse customResponse = new CustomResponse("FAIL", "Username is already existed.");
            return new ResponseEntity<CustomResponse>(customResponse, HttpStatus.CONFLICT);
        }
        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            CustomResponse customResponse = new CustomResponse("FAIL", "Email is already existed.");
            return new ResponseEntity<CustomResponse>(customResponse, HttpStatus.CONFLICT);
        }
        UserModel user = new UserModel(registerRequest.getName(), registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        RoleModel userRole = roleRepository.findByName(RoleName.USER).orElseThrow(()-> new RuntimeException("Role not exist."));
        user.setRoles(Collections.singleton(userRole));
        userRepository.save(user);
        CustomResponse customResponse = new CustomResponse("SUCCESS", "User registered successfully.");
        return new ResponseEntity<CustomResponse>(customResponse, HttpStatus.OK);
    }
    
    /**
     * Login Handler
     * @param loginRequest
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder
            .getContext()
            .setAuthentication(auth);
        String jwt = jwtTokenProvider.generateToken(auth);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", "Bearer " + jwt);
        return new ResponseEntity<>(responseHeaders, HttpStatus.OK); 
    }
}
