package com.szrthk.cbfb.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.szrthk.cbfb.config.JwtService;
import com.szrthk.cbfb.dto.AuthResponse;
import com.szrthk.cbfb.dto.LoginRequest;
import com.szrthk.cbfb.dto.RegisterRequest;
import com.szrthk.cbfb.model.Role;
import com.szrthk.cbfb.model.User;
import com.szrthk.cbfb.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final String bootstrapAdminEmail;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager,
                       @Value("${admin.bootstrap-email:}") String bootstrapAdminEmail) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.bootstrapAdminEmail = bootstrapAdminEmail == null ? "" : bootstrapAdminEmail.toLowerCase();
    }

    public AuthResponse register(RegisterRequest request) {
        if (request.email() == null || request.password() == null) {
            throw new IllegalArgumentException("Email and password are required");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("User already exists");
        }
        Role roleToAssign = request.email().equalsIgnoreCase(bootstrapAdminEmail)
                ? Role.ADMIN
                : Role.USER;
        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(roleToAssign)
                .build();
        @SuppressWarnings("null")
        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser);
        return new AuthResponse(token, savedUser.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getRole().name());
    }

    public void promoteToAdmin(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setRole(Role.ADMIN);
        userRepository.save(user);
    }
}
