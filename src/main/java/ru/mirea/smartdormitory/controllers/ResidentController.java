package ru.mirea.smartdormitory.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
        return String.format("redirect:/residents/%s", authentication.getName());
    }

    @GetMapping("/{student_id}")
    public String viewResident(@PathVariable String student_id, Authentication authentication, Model model) {
        Resident resident = residentService.findByStudentId(student_id);
        resident.setPinCode(null);

        String fio = "";
        if(resident.getSurname() != null)
            fio += resident.getSurname() + " ";
        if (resident.getName() != null)
            fio += resident.getName() + " ";
        if (resident.getPatronymic() != null)
            fio += resident.getPatronymic();

        model.addAttribute("fio", fio);
        model.addAttribute("resident", resident);
        return "resident";
    }

    @PostMapping("/{student_id}")
    public String editResident(@PathVariable String student_id,
                               Authentication authentication,
                               @ModelAttribute("resident") Resident resident)
    {
        RoleType roleType = residentService.getRoleTypeByStudentId(authentication.getName());
        if(roleType == RoleType.COMMANDANT || authentication.getName().equals(student_id))
        {
            if(roleType != RoleType.COMMANDANT)
                resident.setRole(roleType.name());
            residentService.update(residentService.findByStudentId(student_id).getId(), resident);
            return "redirect:/me";
        }
        else
            return "error";
    }
}
