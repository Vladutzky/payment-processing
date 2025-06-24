package com.example.payment_processing.controller;

import com.example.payment_processing.dto.RegisterRequest;
import com.example.payment_processing.exception.UsernameAlreadyExistsException;
import com.example.payment_processing.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 1) Show login form, with optional "registered" flag
    @GetMapping("/login")
    public String showLoginForm(
            Model model,
            @RequestParam(value = "registered", required = false) String registered
    ) {
        if (!model.containsAttribute("loginRequest")) {
            model.addAttribute("loginRequest", new Object()); // dummy, not bound
        }
        if (registered != null) {
            model.addAttribute("successMessage", "Registration successful! Please log in.");
        }
        return "login";
    }

    // 2) Show registration form
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        if (!model.containsAttribute("registerRequest")) {
            model.addAttribute("registerRequest", new RegisterRequest());
        }
        return "register";
    }

    // 3) Handle registration POST
    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("registerRequest") RegisterRequest form,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            authService.register(form);
        } catch (UsernameAlreadyExistsException ex) {
            bindingResult.rejectValue("username", "exists", ex.getMessage());
            return "register";
        }
        // on success, redirect to login with a flag
        return "redirect:/auth/login?registered";
    }
}
