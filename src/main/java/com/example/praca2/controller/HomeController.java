package com.example.praca2.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Metoda do wyświetlania formularza rejestracji
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";  // Wskazuje na widok "register.html"
    }
}