package ru.itis.mainservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.mainservice.dto.request.article.ArticleCreateRequest;
import ru.itis.mainservice.service.AdminArticleService;
import ru.itis.mainservice.service.AuthService;

@Controller
@RequestMapping("/admin/article")
@RequiredArgsConstructor
public class AdminArticleController {

    private final AdminArticleService adminArticleService;
    private final AuthService authService;

    @GetMapping("/new")
    public String getCreateArticlePage() {
        if (!authService.checkAdminRole()) {
            return "redirect:/articles";
        }
        return "admin/article/new";
    }

    @PostMapping("/new")
    public String createArticle(ArticleCreateRequest request, Model model) {
        if (!authService.checkAdminRole()) {
            model.addAttribute("message", "У вас нет прав для создания статей");
            return "error";
        }
        adminArticleService.createArticle(request);
        return "redirect:/articles";
    }
} 