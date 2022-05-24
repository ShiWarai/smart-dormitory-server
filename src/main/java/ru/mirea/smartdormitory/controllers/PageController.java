package ru.mirea.smartdormitory.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.mirea.smartdormitory.model.entities.Resident;
import ru.mirea.smartdormitory.model.repositories.IResidentRepository;
import ru.mirea.smartdormitory.services.ResidentService;

@Controller
@RequestMapping(value = "/page")
public class PageController extends AbstractController<Resident, IResidentRepository> {
    private ResidentService residentService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    protected PageController(ResidentService service) {
        super(service);
        this.residentService = service;
    }

    @GetMapping("/home")
    public String getHome() {
        return "home";
    }
}
