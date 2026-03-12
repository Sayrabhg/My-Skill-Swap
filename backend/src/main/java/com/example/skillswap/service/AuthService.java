package com.example.skillswap.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.skillswap.config.JwtUtil;
import com.example.skillswap.dto.LoginRequest;
import com.example.skillswap.dto.RoleLoginResponse;
import com.example.skillswap.dto.SignupRequest;
import com.example.skillswap.dto.SignupResponse;
import com.example.skillswap.model.User;
import com.example.skillswap.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Signup with role-based assignment
    public SignupResponse signup(SignupRequest request){

        Optional<User> existing = userRepository.findByEmail(request.getEmail());
        if(existing.isPresent()){
            return new SignupResponse("Email already exists", null, request.getEmail(), false);
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setLanguage(request.getLanguage());

        String role = request.getRole();
        String assignedRole = "USER";
        String message = "Signup successful as USER";

        if (role != null && !role.trim().isEmpty()) {

            role = role.toUpperCase().trim();

            if (role.equals("ADMIN")) {
                assignedRole = "ADMIN";
                message = "Signup successful as ADMIN";

            } else if (role.equals("MODERATOR")) {
                assignedRole = "MODERATOR";
                message = "Signup successful as MODERATOR";
                
            } else if (role.equals("MENTOR")) {
            	assignedRole = "MENTOR";
            	message = "Signup successful as MENTOR";

            } else if (role.equals("USER")) {
                assignedRole = "USER";
                message = "Signup successful as USER";
            }
        }

        user.setRole(assignedRole);

        user.setTokens(100);
        user.setTrustScore(0);
        user.setRating(0);

        userRepository.save(user);

        return new SignupResponse(message, assignedRole, user.getEmail(), true);
    }

    // Login with role info
    public RoleLoginResponse login(LoginRequest request){

        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if(userOpt.isEmpty()){
            return new RoleLoginResponse(null, null, "User not found");
        }

        User user = userOpt.get();

        if(!encoder.matches(request.getPassword(), user.getPassword())){
            return new RoleLoginResponse(null, null, "Invalid password");
        }

        String token = JwtUtil.generateToken(user.getEmail());
        String role = user.getRole();

        if (role == null || role.isEmpty()) {
            role = "USER";
        }

        return new RoleLoginResponse(token, role, "Login successful");
    }
}