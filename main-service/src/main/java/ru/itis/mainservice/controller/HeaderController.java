package ru.itis.mainservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HeaderController {

    @GetMapping("/aboutUs")
    public String getAboutUsInfo() {
        return "aboutUs";
    }
}
