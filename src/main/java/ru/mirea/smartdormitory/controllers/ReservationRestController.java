package ru.mirea.smartdormitory.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.mirea.smartdormitory.model.entities.Object;
import ru.mirea.smartdormitory.model.entities.Reservation;
import ru.mirea.smartdormitory.model.types.ObjectType;
import ru.mirea.smartdormitory.model.types.RoleType;
import ru.mirea.smartdormitory.services.ObjectService;
import ru.mirea.smartdormitory.services.ReservationService;
import ru.mirea.smartdormitory.services.ResidentService;
import ru.mirea.smartdormitory.services.StatusTypeService;

import java.sql.Timestamp;
import java.util.List;

@RestController
public class ReservationRestController {

    private final ReservationService reservationService;
    private final ObjectService objectService;
    private final StatusTypeService statusTypeService;
    private final ResidentService residentService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    public ReservationRestController(ReservationService reservationService,
                                     ObjectService objectService,
                                     StatusTypeService statusTypeService,
                                     ResidentService residentService) {
        this.reservationService = reservationService;
        this.objectService = objectService;
        this.statusTypeService = statusTypeService;
        this.residentService = residentService;
    }

    @PostMapping(value = "/reservation/add", consumes = {"application/json"})
    public ResponseEntity<?> createReservation(@RequestBody Reservation reservation) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        Object object = objectService.findById(reservation.getObjectId());
        ObjectType objectType = object.getType();

        if(objectType.getReservationLimit() != null) {
            // Считаем кол-во
            int count = reservationService.findActiveByObjectId(object.getId()).size();
            if((count + 1) > object.getType().getReservationLimit())
                return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }

        if(reservation.getEndReservation().after(time))
            return new ResponseEntity<Long>(reservationService.create(reservation).getId(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.REQUEST_TIMEOUT);
    }

    @GetMapping(value="/reservation/get/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable Long id) {
        Reservation reservation = reservationService.findById(id);
        return reservation != null
                ? new ResponseEntity<>(reservation, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/reservation/get_all")
    public ResponseEntity<List<Reservation>> getReservations() {
        final List<Reservation> reservations = reservationService.getAll();
        return reservations != null && !reservations.isEmpty()
                ? new ResponseEntity<>(reservations, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/reservation/delete/{id}")
    public ResponseEntity<?> deleteReservation(Authentication authentication, @PathVariable Long id) {
        RoleType role = residentService.getResidentRoleByStudentId(authentication.getName());
        Reservation reservation = reservationService.findById(id);

        if(role == RoleType.STUFF || role == RoleType.GUARD || role == RoleType.COMMANDANT) {
            if (reservationService.findById(id) != null && reservationService.delete(id))
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else if(residentService.findById(reservation.getResidentId()).getStudentId() == authentication.getName())
            if (reservationService.findById(id) != null && reservationService.delete(id))
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
