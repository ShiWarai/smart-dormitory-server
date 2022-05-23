package ru.mirea.smartdormitory.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.mirea.smartdormitory.model.entities.Resident;
import ru.mirea.smartdormitory.model.repositories.IResidentRepository;
import ru.mirea.smartdormitory.services.ResidentService;

import java.util.Comparator;

@Controller
@RequestMapping(value = "/user")
public class ResidentController extends AbstractController<Resident, IResidentRepository> {
    private ResidentService residentService;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    protected ResidentController(ResidentService service) {
        super(service);
        this.residentService = residentService;
    }
}
