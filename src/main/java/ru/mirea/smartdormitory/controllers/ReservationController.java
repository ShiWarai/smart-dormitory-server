package ru.mirea.smartdormitory.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.mirea.smartdormitory.model.entities.Reservation;
import ru.mirea.smartdormitory.model.types.RoleType;
import ru.mirea.smartdormitory.services.ReservationService;
import ru.mirea.smartdormitory.services.ResidentService;

@Controller
@RequestMapping(value = "/reservations")
public class ReservationController {
    private ReservationService reservationService;
    private ResidentService residentService;

    @Autowired
    protected ReservationController(ReservationService reservationService, ResidentService residentService) {
        this.reservationService = reservationService;
        this.residentService = residentService;
    }

    @GetMapping("/list")
    public String viewReservations(Authentication authentication, Model model) {
        RoleType role = residentService.getRoleTypeByStudentId(authentication.getName());

        model.addAttribute("role", role.name());
        model.addAttribute("reservations", reservationService.getAll());
        return "reservations";
    }

    @GetMapping("/create")
    public String viewCreationOfReservation(Authentication authentication, Model model) {
        RoleType role = residentService.getRoleTypeByStudentId(authentication.getName());

        Reservation reservation = new Reservation();
        reservation.setResidentId(residentService.findByStudentId(authentication.getName()).getId());

        model.addAttribute("role", role.name());
        model.addAttribute("reservation", reservation);
        return "create_reservation";
    }

    @PostMapping("/create")
    public String createReservation(@ModelAttribute("reservation") Reservation reservation) {
        if(reservationService.create(reservation) != null)
            return "redirect:/reservations/" + reservation.getId();
        else
            return "error";
    }

    @GetMapping("/{id}")
    public String viewReservation(@PathVariable Long id, Authentication authentication, Model model) {
        RoleType role = residentService.getRoleTypeByStudentId(authentication.getName());

        Reservation reservation = reservationService.findById(id);

        model.addAttribute("role", role.name());
        model.addAttribute("reservation", reservation);
        return "reservation";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('COMMANDANT', 'STUFF')")
    public String editReservation(@PathVariable Long id,
                               @ModelAttribute("reservation") Reservation reservation)
    {
        Reservation old_reservation = reservationService.findById(id);

        if(old_reservation != null) {
            reservation.setId(id);

            reservationService.update(reservation.getId(), reservation);

            return "redirect:/reservations/list";
        }
        else
            return "redirect:/error";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('COMMANDANT', 'STUFF')")
    public String deleteReservation(@PathVariable Long id) {
        Reservation reservation = reservationService.findById(id);

        if (reservation != null && reservationService.delete(reservation.getId()))
            return "redirect:/reservations/list";
        else
            return "error";
    }
}
