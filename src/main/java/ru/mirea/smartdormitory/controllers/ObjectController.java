package ru.mirea.smartdormitory.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.mirea.smartdormitory.model.entities.Object;
import ru.mirea.smartdormitory.model.repositories.IObjectRepository;
import ru.mirea.smartdormitory.model.types.RoleType;
import ru.mirea.smartdormitory.services.ObjectService;
import ru.mirea.smartdormitory.services.ResidentService;

import java.sql.Date;
import java.text.SimpleDateFormat;

@Controller
@RequestMapping(value = "/objects")
public class ObjectController{
    private ObjectService objectService;
    private ResidentService residentService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    protected ObjectController(ObjectService objectService, ResidentService residentService) {
        this.objectService = objectService;
        this.residentService = residentService;
    }

    @GetMapping("/list")
    public String viewObjects(Authentication authentication, Model model) {
        RoleType role = residentService.getRoleTypeByStudentId(authentication.getName());

        model.addAttribute("role", role.name());
        model.addAttribute("objects", objectService.getAll());
        return "objects";
    }

    @GetMapping("/{id}")
    public String viewObject(@PathVariable Long id, Authentication authentication, Model model) {
        RoleType role = residentService.getRoleTypeByStudentId(authentication.getName());

        Object object = objectService.findById(id);

        model.addAttribute("role", role.name());
        model.addAttribute("object", object);
        return "object";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('COMMANDANT', 'STUFF')")
    public String editObject(@PathVariable Long id,
                               @ModelAttribute("object") Object object)
    {
        Object old_object = objectService.findById(id);

        if(old_object != null) {
            object.setId(id);

            objectService.update(object.getId(), object);

            return "redirect:/objects/list";
        }
        else
            return "redirect:/error";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('COMMANDANT', 'STUFF')")
    public String deleteObject(@PathVariable Long id) {
        Object object = objectService.findById(id);

        if (object != null && objectService.delete(object.getId()))
            return "redirect:/objects/list";
        else
            return "error";
    }
}
