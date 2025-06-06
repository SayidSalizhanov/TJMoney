package ru.itis.mainservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.itis.mainservice.dto.request.user.UserLoginRequest;
import ru.itis.mainservice.dto.request.user.UserRegisterRequest;
import ru.itis.mainservice.service.AuthService;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final RestTemplate restTemplate;
    private final String apiUrl = "http://localhost:8080";
    private final AuthService authService;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(UserLoginRequest request, RedirectAttributes redirectAttributes) {
        try {
            authService.login(request);
            redirectAttributes.addFlashAttribute("success", "Login successful!");
            return "redirect:/articles";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Login failed: " + e.getMessage());
            return "redirect:/login";
        }
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(UserRegisterRequest request, RedirectAttributes redirectAttributes) {
        try {
            restTemplate.postForEntity(apiUrl + "/api/auth/register", request, Long.class);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Registration failed: " + e.getMessage());
            return "redirect:/register";
        }
    }

    @PostMapping("/logout")
    public String logout() {
        authService.logout();
        return "redirect:/login";
    }
} 