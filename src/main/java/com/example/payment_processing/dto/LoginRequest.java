package com.example.payment_processing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {
    @NotBlank(message = "Username is required")
    @Size(min=3, max=50)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min=6)
    private String password;

    public LoginRequest() { }

    // Add this:
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
