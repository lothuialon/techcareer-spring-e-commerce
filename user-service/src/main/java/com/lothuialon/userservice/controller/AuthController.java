package com.lothuialon.userservice.controller;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lothuialon.userservice.DTO.JwtAuthenticationResponse;
import com.lothuialon.userservice.DTO.loginDTO;
import com.lothuialon.userservice.DTO.registerDTO;
import com.lothuialon.userservice.service.authService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private authService authService;

    public AuthController(authService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody loginDTO loginDTO){

        String token = authService.login(loginDTO);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setAccessToken(token);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        return ResponseEntity.ok().headers(headers).body(jwtAuthenticationResponse);

    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody registerDTO registerDTO){

        String response = authService.register(registerDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }


}


