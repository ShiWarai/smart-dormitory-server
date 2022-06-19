package ru.mirea.smartdormitory.controllers.basic;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.mirea.smartdormitory.services.ResidentService;

import javax.net.ssl.SSLEngineResult;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class BaseController {
    private final ResidentService residentService;

    protected BaseController(ResidentService residentService) {
        this.residentService = residentService;
    }

    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}