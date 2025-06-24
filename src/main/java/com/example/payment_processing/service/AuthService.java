package com.example.payment_processing.service;

import com.example.payment_processing.dto.LoginRequest;
import com.example.payment_processing.dto.RegisterRequest;
import com.example.payment_processing.model.User;
import com.example.payment_processing.repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbc;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            javax.sql.DataSource dataSource
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jdbc = new JdbcTemplate(dataSource);
    }

    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists.");
        }
        User user = new User(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                true
        );
        userRepository.save(user);

        // <-- insert authority so Spring Security will see ROLE_USER
        String sql = "INSERT INTO authorities (username, authority) VALUES (?, ?)";
        jdbc.update(sql,
                request.getUsername(),
                "ROLE_USER"
        );
    }

    public void login(LoginRequest request) {
        // if you're still using formLogin+session, you can remove your JWT logic here
        // Spring Security's default UsernamePasswordAuthenticationFilter
        // will handle the actual authentication for you.
    }
}
