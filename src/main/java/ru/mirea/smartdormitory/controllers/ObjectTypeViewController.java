package ru.mirea.smartdormitory.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.mirea.smartdormitory.model.types.ObjectType;
import ru.mirea.smartdormitory.model.types.RoleType;
import ru.mirea.smartdormitory.services.*;

@Controller
@RequestMapping(value = "/object_types")
public class ObjectTypeViewController {
    private ObjectService objectService;
    private ResidentService residentService;
    private RoomService roomService;
    private ObjectTypeService objectTypeService;
    private StatusTypeService statusTypeService;

    @Autowired
    protected ObjectTypeViewController(ObjectService objectService,
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
    @PreAuthorize("hasAnyAuthority('COMMANDANT', 'STUFF')")
    public String viewObjectTypes(Authentication authentication, Model model) {
        RoleType role = residentService.getRoleTypeByStudentId(authentication.getName());

        model.addAttribute("role", role.name());
        model.addAttribute("object_types", objectTypeService.getAll());
        return "object_types";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyAuthority('COMMANDANT', 'STUFF')")
    public String viewCreationOfObjectType(Authentication authentication, Model model) {
        RoleType role = residentService.getRoleTypeByStudentId(authentication.getName());

        ObjectType objectType = new ObjectType();

        model.addAttribute("object_type", objectType);
        model.addAttribute("role", role.name());
        return "create_object_type";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('COMMANDANT', 'STUFF')")
    public String createObjectType(@ModelAttribute("object_type") ObjectType objectType) {
        if(objectType.getSchedule().isBlank())
            objectType.setSchedule(null);
        
        if(objectTypeService.create(objectType) != null)
            return "redirect:/object_types/" + objectType.getId();
        else
            return "error";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('COMMANDANT', 'STUFF')")
    public String viewObjectType(@PathVariable Long id, Authentication authentication, Model model) {
        RoleType role = residentService.getRoleTypeByStudentId(authentication.getName());

        ObjectType objectType = objectTypeService.findById(id);

        model.addAttribute("object_type", objectType);
        model.addAttribute("role", role.name());
        return "object_type";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('COMMANDANT', 'STUFF')")
    public String editObjectType(@PathVariable Long id,
                               @ModelAttribute("object_type") ObjectType object_type)
    {
        ObjectType old_objectType = objectTypeService.findById(id);

        if(old_objectType != null) {
            object_type.setId(id);

            objectTypeService.update(object_type.getId(), object_type);

            return "redirect:/object_types/list";
        }
        else
            return "error";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('COMMANDANT', 'STUFF')")
    public String deleteObjectType(@PathVariable Long id) {
        ObjectType objectType = objectTypeService.findById(id);

        if (objectType != null && objectTypeService.delete(objectType.getId()))
            return "redirect:/object_types/list";
        else
            return "error";
    }
}
