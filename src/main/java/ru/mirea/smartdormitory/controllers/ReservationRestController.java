package ru.mirea.smartdormitory.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.mirea.smartdormitory.model.entities.Object;
import ru.mirea.smartdormitory.model.entities.Reservation;
import ru.mirea.smartdormitory.model.types.ObjectType;
import ru.mirea.smartdormitory.model.types.RoleType;
import ru.mirea.smartdormitory.services.ObjectService;
import ru.mirea.smartdormitory.services.ReservationService;
import ru.mirea.smartdormitory.services.ResidentService;

import javax.management.relation.Role;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping(value = "/reservation")
public class ReservationRestController {

    private final ReservationService reservationService;
    private final ObjectService objectService;
    private final ResidentService residentService;

    @Autowired
    public ReservationRestController(ReservationService reservationService,
                                     ObjectService objectService,
                                     ResidentService residentService) {
        this.reservationService = reservationService;
        this.objectService = objectService;
        this.residentService = residentService;
    }

    @PostMapping(value = "/", consumes = {"application/json"})
    public ResponseEntity<?> createReservation(Authentication authentication, @RequestBody Reservation reservation) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        RoleType role = residentService.getRoleTypeByStudentId(authentication.getName());
        Object object = objectService.findById(reservation.getObjectId());
        ObjectType objectType = object.getType();

        if(objectType.getReservationLimit() != null) {
            // Считаем кол-во
            int count = reservationService.findActiveByObjectId(object.getId()).size();
            if((count + 1) > object.getType().getReservationLimit())
                return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }

        if(role != RoleType.COMMANDANT)
            reservation.setResidentId(residentService.findByStudentId(authentication.getName()).getId());

        if(reservation.getEndReservation().after(time))
            return new ResponseEntity<Long>(reservationService.create(reservation).getId(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.REQUEST_TIMEOUT);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable Long id) {
        Reservation reservation = reservationService.findById(id);
        return reservation != null
                ? new ResponseEntity<>(reservation, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/all")
    public ResponseEntity<List<Reservation>> getReservations() {
        final List<Reservation> reservations = reservationService.getAll();
        return reservations != null && !reservations.isEmpty()
                ? new ResponseEntity<>(reservations, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservation(Authentication authentication, @PathVariable Long id) {
        RoleType role = residentService.getRoleTypeByStudentId(authentication.getName());
        Reservation reservation = reservationService.findById(id);

        if(role.ordinal() >= RoleType.STUFF.ordinal()) {
            if (reservationService.findById(id) != null && reservationService.delete(id))
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else if(residentService.findById(reservation.getResidentId()).getStudentId().equals(authentication.getName()))
            if (reservationService.findById(id) != null && reservationService.delete(id))
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
