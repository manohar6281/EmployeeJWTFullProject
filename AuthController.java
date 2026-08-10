package com.nit.controller;

import com.nit.security.JwtUtil;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {


        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.username(),
                                request.password()
                        )
                );

    
        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();

       
        String token =
                jwtUtil.generateToken(userDetails);

      
        String role =
                userDetails.getAuthorities()
                        .iterator()
                        .next()
                        .getAuthority()
                        .replace("ROLE_", "");

        return ResponseEntity.ok(
                new LoginResponse(
                        token,
                        userDetails.getUsername(),
                        role
                )
        );
    }

   
    public record LoginRequest(

            @NotBlank
            String username,

            @NotBlank
            String password
    ) {
    }

  
    public record LoginResponse(

            String token,
            String username,
            String role
    ) {
    }
}