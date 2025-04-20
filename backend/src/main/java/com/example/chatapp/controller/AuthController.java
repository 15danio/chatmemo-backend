package com.example.chatapp.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.chatapp.config.JwtUtils;
import com.example.chatapp.entity.User;
import com.example.chatapp.repository.UserRepository;



@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("Inscription réussie !");
    }

    @PostMapping("/login")
public ResponseEntity<String> login(@RequestBody User user) {
    User existingUser = userRepository.findByUsername(user.getUsername());
    if (existingUser == null) {
        return ResponseEntity.status(401).body("Utilisateur non trouvé");
    }

    if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
        return ResponseEntity.status(401).body("Mot de passe incorrect");
    }
    
    String token = jwtUtils.generateToken(
        new org.springframework.security.core.userdetails.User(
            existingUser.getUsername(),
            existingUser.getPassword(), // Utilisez le mot de passe hashé ici
            new ArrayList<>()
        )
    );
    return ResponseEntity.ok(token);
}
}