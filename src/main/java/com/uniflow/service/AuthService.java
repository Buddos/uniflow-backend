package com.uniflow.service;

import com.uniflow.model.User;
import com.uniflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DataChangePublisher dataChangePublisher;
    
    public User authenticateUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isEmpty()) {
            return null;
        }
        
        User user = userOpt.get();
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }
        
        if (!user.getIsActive()) {
            throw new RuntimeException("Account is deactivated. Please contact administrator.");
        }
        
        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        
        return user;
    }
    
    public User register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
        
        User saved = userRepository.save(user);
        dataChangePublisher.publishUserCreated(saved);
        return saved;
    }
    
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    public boolean validateSession(Long userId, String sessionId) {
        // Additional session validation logic
        Optional<User> userOpt = userRepository.findById(userId);
        return userOpt.isPresent() && userOpt.get().getIsActive();
    }
}