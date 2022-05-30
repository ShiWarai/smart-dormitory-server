package ru.mirea.smartdormitory.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.mirea.smartdormitory.model.entities.Object;
import ru.mirea.smartdormitory.model.entities.Reservation;
import ru.mirea.smartdormitory.model.entities.Resident;
import ru.mirea.smartdormitory.model.types.ObjectType;
import ru.mirea.smartdormitory.model.types.RoleType;
import ru.mirea.smartdormitory.services.ObjectService;
import ru.mirea.smartdormitory.services.ReservationService;
import ru.mirea.smartdormitory.services.ResidentService;

import javax.management.relation.Role;
import java.sql.Timestamp;

@Controller
@RequestMapping(value = "/reservations")
public class ReservationController {
    private ReservationService reservationService;
    private ResidentService residentService;
    private ObjectService objectService;

    @Autowired
    protected ReservationController(ReservationService reservationService, ResidentService residentService, ObjectService objectService) {
        this.reservationService = reservationService;
        this.residentService = residentService;
        this.objectService = objectService;
    }

    @GetMapping("/list")
    public String viewReservations(Authentication authentication, Model model) {
        RoleType role = residentService.getRoleTypeByStudentId(authentication.getName());

        model.addAttribute("role", role.name());
        model.addAttribute("reservations", reservationService.getAll());
        return "reservations";
    }

    @GetMapping("/my")
    public String viewMyReservations(Authentication authentication, Model model) {
        Resident resident = residentService.findByStudentId(authentication.getName());

        model.addAttribute("role", resident.getRole());
        model.addAttribute("reservations", reservationService.findAllByResidentId(resident.getId()));
        return "reservations";
    }

    @GetMapping("/create")
    public String viewCreationOfReservation(Authentication authentication, Model model) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        RoleType role = residentService.getRoleTypeByStudentId(authentication.getName());

        Reservation reservation = new Reservation();
        reservation.setResidentId(residentService.findByStudentId(authentication.getName()).getId());
        reservation.setStartReservation(time);
        reservation.setEndReservation(time);

        model.addAttribute("role", role.name());
        model.addAttribute("reservation", reservation);
        return "create_reservation";
    }

    @PostMapping("/create")
    public String createReservation(Authentication authentication, @ModelAttribute("reservation") Reservation reservation) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        RoleType role = residentService.getRoleTypeByStudentId(authentication.getName());
        Object object = objectService.findById(reservation.getObjectId());
        ObjectType objectType = object.getType();

        if(objectType.getReservationLimit() != null) {
            // Считаем кол-во
            int count = reservationService.findActiveByObjectId(object.getId()).size();
            if((count + 1) > object.getType().getReservationLimit())
                return "error";
        }

        if(role != RoleType.COMMANDANT)
            reservation.setResidentId(residentService.findByStudentId(authentication.getName()).getId());

        if(reservation.getEndReservation().after(time) && reservationService.create(reservation) != null)
            return "redirect:/reservations/" + reservation.getId();
        else
            return "error";
    }

    @GetMapping("/{id}")
    public String viewReservation(@PathVariable Long id, Authentication authentication, Model model) {
        Resident resident = residentService.findByStudentId(authentication.getName());
        String role = resident.getRole();

        Reservation reservation = reservationService.findById(id);
        boolean canDelete = (reservation.getResidentId() == resident.getId()) || (role.equals(RoleType.COMMANDANT.name()));

        model.addAttribute("role", role);
        model.addAttribute("canDelete", canDelete);
        model.addAttribute("reservation", reservation);
        return "reservation";
    }

    @GetMapping("/delete/{id}")
    public String deleteReservation(Authentication authentication, @PathVariable Long id) {
        Reservation reservation = reservationService.findById(id);
        RoleType role = residentService.getRoleTypeByStudentId(authentication.getName());

        if(role.ordinal() >= RoleType.STUFF.ordinal()) {
            if (reservationService.findById(id) != null && reservationService.delete(id))
                return "redirect:/reservations/list";
            else
                return "error";
        }
        else if(residentService.findById(reservation.getResidentId()).getStudentId().equals(authentication.getName()))
            if (reservationService.findById(id) != null && reservationService.delete(id))
                return "redirect:/reservations/list";
            else
                return "error";
        else
            return "error";
    }
}
