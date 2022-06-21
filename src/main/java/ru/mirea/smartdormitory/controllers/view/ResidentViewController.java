package ru.mirea.smartdormitory.controllers.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.mirea.smartdormitory.model.entities.Resident;
import ru.mirea.smartdormitory.model.types.RoleType;
import ru.mirea.smartdormitory.services.ResidentService;
import ru.mirea.smartdormitory.services.RoomService;

import java.sql.Date;
import java.text.SimpleDateFormat;

@Controller
@RequestMapping(value = "/residents")
public class ResidentViewController {
    private ResidentService residentService;
    private RoomService roomService;

    @Autowired
    protected ResidentViewController(ResidentService residentService, RoomService roomService) {
        this.residentService = residentService;
        this.roomService = roomService;
    }

    @GetMapping("/me")
    public String getMy(Authentication authentication) {
        return String.format("redirect:/residents/%s", authentication.getName());
    }

    @GetMapping("/list")
    public String viewResidents(Authentication authentication, Model model) {
        RoleType role = residentService.getRoleTypeByStudentId(authentication.getName());

        model.addAttribute("role", role.name());
        model.addAttribute("residents", residentService.getAll());
        return "residents";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyAuthority('COMMANDANT')")
    public String viewCreationOfResident(Authentication authentication, Model model) {
        RoleType role = residentService.getRoleTypeByStudentId(authentication.getName());

        Resident resident = new Resident();

        model.addAttribute("rooms", roomService.getAll());
        model.addAttribute("role", role.name());
        model.addAttribute("resident", resident);
        return "create_resident";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('COMMANDANT')")
    public String createResident(@ModelAttribute("resident") Resident resident) {
        if(residentService.create(resident) != null)
            return "redirect:/residents/" + resident.getStudentId();
        else
            return "error";
    }

    @GetMapping("/{student_id}")
    public String viewResident(@PathVariable String student_id, Authentication authentication, Model model) {
        RoleType role = residentService.getRoleTypeByStudentId(authentication.getName());

        Resident resident = residentService.getByStudentId(student_id);

        int accessLevel = 0;
        if(authentication.getName().equals(student_id))
            accessLevel = 1;
        if(role.ordinal() >= RoleType.GUARD.ordinal() && !student_id.equals(authentication.getName()))
            accessLevel = 3;

        model.addAttribute("rooms", roomService.getAll());
        model.addAttribute("role", role.name());
        model.addAttribute("accessLevel", accessLevel);
        model.addAttribute("resident", resident);
        return "resident";
    }

    @PostMapping("/{student_id}")
    public String editResident(@PathVariable String student_id,
                               Authentication authentication,
                               @ModelAttribute("resident") Resident resident)
    {
        RoleType role = residentService.getRoleTypeByStudentId(authentication.getName());

        if(role == RoleType.COMMANDANT || authentication.getName().equals(student_id))
        {
            Resident old_resident = residentService.getByStudentId(resident.getStudentId());

            if(old_resident != null) {
                if (role != RoleType.COMMANDANT)
                    resident.setRole(old_resident.getRole());
                if (resident.getPinCode().isBlank())
                    resident.setPinCode(old_resident.getPinCode());

                residentService.update(residentService.getByStudentId(student_id).getId(), resident);

                if(resident.getStudentId().equals(authentication.getName()))
                    return "redirect:/residents/me";
                else
                    return "redirect:/residents/" + student_id;
            }
            else
                return "redirect:/error";
        }
        else
            return "error";
    }

    @GetMapping("/delete/{student_id}")
    @PreAuthorize("hasAnyAuthority('COMMANDANT')")
    public String deleteResident(@PathVariable String student_id) {
        Resident resident = residentService.getByStudentId(student_id);

        if (resident != null && residentService.delete(resident.getId()))
            return "redirect:/residents/list";
        else
            return "error";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
