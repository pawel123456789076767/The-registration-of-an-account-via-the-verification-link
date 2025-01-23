package com.example.praca2.controller;

import com.example.praca2.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class RegisterController {


    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String email) {
        try {
            userService.registerUser(email);
            return ResponseEntity.ok("Rejestracja zakończona sukcesem! Sprawdź skrzynkę e-mail, aby znaleźć link aktywacyjny.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activate(@RequestParam String token) {
        try {
            userService.activateUser(token);
            return ResponseEntity.ok("Konto zostało zwerifikowane!");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/is-verified")
    public ResponseEntity<Boolean> isVerified(@RequestParam String email) {
        boolean verified = userService.isUserVerified(email);
        return ResponseEntity.ok(verified);
    }
}