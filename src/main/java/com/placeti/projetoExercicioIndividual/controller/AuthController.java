package com.placeti.projetoExercicioIndividual.controller;
import com.placeti.projetoExercicioIndividual.dto.LoginRequest;
import com.placeti.projetoExercicioIndividual.dto.LoginResponse;
import com.placeti.projetoExercicioIndividual.dto.RegisterUserRequest;
import com.placeti.projetoExercicioIndividual.dto.RegisterUserResponse;
import com.placeti.projetoExercicioIndividual.services.AuthService;
import com.placeti.projetoExercicioIndividual.services.CustomUserDetailsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
       private final AuthService authService;
    private final String beanDaYasmin;

    public AuthController( AuthService authService, String beanDaYasmin) {
        this.authService = authService;
        this.beanDaYasmin = beanDaYasmin;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest)
    {
        LoginResponse loginResponse = authService.login(loginRequest) ;
        return ResponseEntity.ok().body(loginResponse);
    }
    @PostMapping("/auth/register")
    public ResponseEntity<RegisterUserResponse> registerUser(@Valid @RequestBody RegisterUserRequest registerUserRequest)
    {
        RegisterUserResponse registerUserResponse = authService.registerUser(registerUserRequest);
        return ResponseEntity.ok().body(registerUserResponse);
    }
}
