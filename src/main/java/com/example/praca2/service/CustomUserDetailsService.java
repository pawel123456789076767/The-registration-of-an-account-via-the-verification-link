package com.example.praca2.service;


import com.example.praca2.model.User;
import com.example.praca2.repo.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Znajdź użytkownika po e-mailu
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    // Jeżeli użytkownik nie istnieje w bazie danych
                    System.out.println("User not found for email: " + email);
                    return new UsernameNotFoundException("User not found");
                });

        // Sprawdzenie, czy użytkownik jest zweryfikowany
        if (!user.isVerified()) {
            System.out.println("User email not verified: " + email);
            throw new UsernameNotFoundException("User email is not verified");
        }

        // Logowanie użytkownika bez hasła (puste hasło)
        System.out.println("User found and verified: " + email);
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail()) // E-mail jako nazwa użytkownika
                .password("{noop}") // Hasło jako "noop" (bez kodowania) - ważne dla Spring Security
                .roles("USER") // Rola użytkownika (można to zmienić w zależności od roli)
                .build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

}