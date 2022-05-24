package ru.mirea.smartdormitory.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.mirea.smartdormitory.model.entities.Resident;
import ru.mirea.smartdormitory.model.repositories.IResidentRepository;
import ru.mirea.smartdormitory.model.types.RoleType;
import ru.mirea.smartdormitory.services.ResidentService;

@Controller
@RequestMapping(value = "/residents")
public class ResidentController extends AbstractController<Resident, IResidentRepository> {
    private ResidentService residentService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    protected ResidentController(ResidentService service) {
        super(service);
        this.residentService = service;
    }

    @GetMapping("/me")
    public String getMy(Authentication authentication) {
        return String.format("redirect:/residents/view_page/%s", authentication.getName());
    }

    @GetMapping("/view_page/{student_id}")
    public String editResident(@PathVariable String student_id, Authentication authentication, Model model) {
        RoleType role = residentService.getResidentRoleByStudentId(authentication.getName());
        if(role == RoleType.COMMANDANT || role == RoleType.GUARD || authentication.getName().equals(student_id))
        {
            model.addAttribute("userRole", role.name());
            model.addAttribute("resident", residentService.findResidentByStudentId(student_id));
            return "view";
        }
        else
            return "error";
    }
}
