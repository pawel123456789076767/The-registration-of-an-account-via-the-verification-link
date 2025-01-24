package com.example.praca2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    // Metoda do wyświetlania formularza rejestracji
    @GetMapping("/index")
    public String idex() {
        return "index";  // Wskazuje na widok "register.html"
    }
}