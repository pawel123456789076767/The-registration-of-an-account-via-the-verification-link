package com.example.praca2.controller;

import com.example.praca2.model.User;
import com.example.praca2.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActivationController {
    private static final Logger logger = LoggerFactory.getLogger(ActivationController.class);

    @Autowired
    private UserRepository userRepository; // Wstrzyknięcie repozytorium, które będzie używane do komunikacji z bazą danych

    @GetMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestParam("token") String token) {
        System.out.println("Received token: " + token);  // Logowanie tokenu

        User user = userRepository.findByActivationToken(token).orElse(null);
        if (user != null) {
            System.out.println("User found with email: " + user.getEmail());  // Logowanie użytkownika
        } else {
            System.out.println("No user found for token: " + token);  // Logowanie błędu
        }

        if (user != null && !user.isVerified()) {
            // Aktywujemy konto
            user.setVerified(true);
            user.setActivationToken(null);  // Usuwamy token aktywacyjny
            userRepository.save(user);  // Zapisujemy użytkownika zaktualizowanego

            // Wyświetlenie komunikatu i przekierowanie
            String htmlResponse = "<html>" +
                    "<body>" +
                    "<h1>Konto zostało zweryfikowane</h1>" +
                    "<p>Za chwilę nastąpi przekierowanie na stronę logowania...</p>" +
                    "<script>setTimeout(function() { window.location.href = '/login'; }, 3000);</script>" +
                    "</body>" +
                    "</html>";
            return ResponseEntity.ok(htmlResponse);
        } else if (user != null && user.isVerified()) {
            return ResponseEntity.badRequest().body("Konto jest już zweryfikowane.");
        } else {
            return ResponseEntity.badRequest().body("Nieprawidłowy link aktywacyjny lub token.");
        }
    }}