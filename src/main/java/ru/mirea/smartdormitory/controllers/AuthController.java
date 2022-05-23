package ru.mirea.smartdormitory.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.mirea.smartdormitory.model.entities.Resident;
import ru.mirea.smartdormitory.model.repositories.IResidentRepository;
import ru.mirea.smartdormitory.services.ResidentService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class AuthController extends AbstractController<Resident, IResidentRepository>{
    private final ResidentService residentService;

    protected AuthController(ResidentService service) {
        super(service);
        this.residentService = service;
    }

    @GetMapping("/login-error")
    public String login(Authentication authentication,
                        HttpServletRequest request,
                        Model model) {
        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                errorMessage = ex.getMessage();
            }
        }
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("userRole", residentService.getResidentRoleByStudentId(authentication.getName()));
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }

//    @GetMapping("/sign")
//    public String getRegistrationPage(@ModelAttribute("resident") Resident resident) {
//        return "registration";
//    }
}