package ru.mirea.smartdormitory.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.mirea.smartdormitory.model.entities.Object;
import ru.mirea.smartdormitory.model.types.RoleType;
import ru.mirea.smartdormitory.model.types.StatusType;
import ru.mirea.smartdormitory.services.*;

@Controller
@RequestMapping(value = "/objects")
public class ObjectController{
    private ObjectService objectService;
    private ResidentService residentService;
    private RoomService roomService;
    private ObjectTypeService objectTypeService;
    private StatusTypeService statusTypeService;

    @Autowired
    protected ObjectController(ObjectService objectService,
                               ResidentService residentService,
                               RoomService roomService,
                               ObjectTypeService objectTypeService,
                               StatusTypeService statusTypeService) {
        this.objectService = objectService;
        this.residentService = residentService;
        this.roomService = roomService;
        this.objectTypeService = objectTypeService;
        this.statusTypeService = statusTypeService;
    }

    @GetMapping("/list")
    public String viewObjects(Authentication authentication, Model model) {
        RoleType role = residentService.getRoleTypeByStudentId(authentication.getName());

        model.addAttribute("role", role.name());
        model.addAttribute("objects", objectService.getAll());
        return "objects";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyAuthority('COMMANDANT', 'STUFF')")
    public String viewCreationOfObject(Authentication authentication, Model model) {
        RoleType role = residentService.getRoleTypeByStudentId(authentication.getName());

        Object object = new Object();

        model.addAttribute("statuses", statusTypeService.getAll());
        model.addAttribute("types", objectTypeService.getAll());
        model.addAttribute("rooms", roomService.getAll());
        model.addAttribute("role", role.name());
        model.addAttribute("object", object);
        return "create_object";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('COMMANDANT', 'STUFF')")
    public String createObject(@ModelAttribute("object") Object object) {
        if(objectService.create(object) != null)
            return "redirect:/objects/" + object.getId();
        else
            return "error";
    }

    @GetMapping("/{id}")
    public String viewObject(@PathVariable Long id, Authentication authentication, Model model) {
        RoleType role = residentService.getRoleTypeByStudentId(authentication.getName());

        Object object = objectService.findById(id);

        model.addAttribute("statuses", statusTypeService.getAll());
        model.addAttribute("types", objectTypeService.getAll());
        model.addAttribute("rooms", roomService.getAll());
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
