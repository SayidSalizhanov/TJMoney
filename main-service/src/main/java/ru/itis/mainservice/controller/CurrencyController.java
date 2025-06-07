package ru.itis.mainservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CurrencyController {

    @GetMapping("/currency")
    public String getCurrencyPage() {
        return "currency";
    }
} 