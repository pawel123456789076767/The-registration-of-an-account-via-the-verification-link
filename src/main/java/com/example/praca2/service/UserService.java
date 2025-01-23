package com.example.praca2.service;

import com.example.praca2.model.User;
import com.example.praca2.repo.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    public UserService(UserRepository userRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    public User registerUser(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email jest już zarejestrowany!");
        }

        User user = new User();
        user.setEmail(email);
        user.setVerified(false);
        user.setActivationToken(UUID.randomUUID().toString());
        userRepository.save(user);

        sendActivationEmail(user);
        return user;
    }

    private void sendActivationEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Activate your account");
        message.setText("Kliknij w link aktywacyjny by aktywować konto: " +
                "http://localhost:8080/activate?token=" + user.getActivationToken());
        mailSender.send(message);
    }

    public void activateUser(String token) {
        User user = userRepository.findByActivationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Niepoprawny token weryfikacyjny!"));
        user.setVerified(true);
        user.setActivationToken(null);
        userRepository.save(user);
    }

    public boolean isUserVerified(String email) {
        return userRepository.findByEmail(email)
                .map(User::isVerified)
                .orElse(false);
    }
}