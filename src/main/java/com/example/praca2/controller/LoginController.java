package com.example.praca2.controller;

import com.example.praca2.model.User;
import com.example.praca2.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    // Strona logowania
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // Obsługa logowania
    @PostMapping("/login")
    public String handleLogin(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        // Sprawdź, czy użytkownik istnieje w bazie danych
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Nie znaleziono użytkownika z podanym adresem email.");
            return "redirect:/login";  // Wróć na stronę logowania z komunikatem błędu
        }

        User user = userOptional.get();

        // Sprawdzenie, czy konto zostało zweryfikowane
        if (!user.isVerified()) {
            redirectAttributes.addFlashAttribute("error", "Konto nie zostało zweryfikowane. Proszę zweryfikować konto przed logowaniem.");
            return "redirect:/login";  // Wróć na stronę logowania z komunikatem błędu
        }

        // Jeśli użytkownik jest zweryfikowany, zaloguj go i przekieruj do welcome
        redirectAttributes.addFlashAttribute("message", "Logowanie zakończone sukcesem!");
        return "redirect:/login";  // Przekierowanie na stronę powitalną
    }
}