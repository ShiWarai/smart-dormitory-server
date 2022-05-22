package ru.mirea.smart_dormitory.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.mirea.smart_dormitory.services.UserAppService;
import ru.mirea.smart_dormitory.tables.User;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class AuthController {
    private UserAppService applicationUserService;

    @GetMapping("login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("")
    public String getIndexPage() {
        return "home";
    }

    @GetMapping("registration")
    public String getRegistrationPage(@ModelAttribute("user") User user) {
        return "registration";
    }

    @PostMapping("registration")
    public String signUpUser(@ModelAttribute("user") User user) {
        return applicationUserService.signUpUser(user);
    }
}