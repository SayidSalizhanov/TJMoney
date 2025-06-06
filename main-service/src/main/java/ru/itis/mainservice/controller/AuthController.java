package ru.itis.mainservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String login(@Valid UserLoginRequest request, Model model) {
        try {
            boolean status = authService.login(request);
            if (status) {
                return "redirect:/user";
            }
            model.addAttribute("error", "Неверный email или пароль");
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", "Неверный email или пароль");
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid UserRegisterRequest request, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .findFirst()
                    .orElse("Ошибка валидации");
            model.addAttribute("error", errorMessage);
            return "register";
        }

        try {
            restTemplate.postForEntity(apiUrl + "/api/auth/register", request, Long.class);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при регистрации: " + e.getMessage());
            return "register";
        }
    }

    @PostMapping("/logout")
    public String logout() {
        authService.logout();
        return "redirect:/login";
    }
} 