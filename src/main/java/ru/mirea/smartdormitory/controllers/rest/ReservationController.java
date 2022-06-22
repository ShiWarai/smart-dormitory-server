package ru.mirea.smartdormitory.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.mirea.smartdormitory.model.entities.Object;
import ru.mirea.smartdormitory.model.entities.Reservation;
import ru.mirea.smartdormitory.model.request_bodies.ReservationBody;
import ru.mirea.smartdormitory.model.types.ObjectType;
import ru.mirea.smartdormitory.model.types.RoleType;
import ru.mirea.smartdormitory.services.ObjectService;
import ru.mirea.smartdormitory.services.ReservationService;
import ru.mirea.smartdormitory.services.ResidentService;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping(value = "/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final ObjectService objectService;
    private final ResidentService residentService;

    @Autowired
    public ReservationController(ReservationService reservationService,
                                 ObjectService objectService,
                                 ResidentService residentService) {
        this.reservationService = reservationService;
        this.objectService = objectService;
        this.residentService = residentService;
    }

    @PostMapping(value = "/", consumes = {"application/json"})
    public ResponseEntity<?> createReservation(Authentication authentication, @RequestBody ReservationBody reservation) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        Object object = objectService.getById(reservation.getObjectId());

        if(!objectService.canBeReserved(object, reservationService))
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);

        // There is better to use adapter
        Reservation reservation_entity = new Reservation();

        reservation_entity.setObjectId(reservation.getObjectId());
        reservation_entity.setReason(reservation.getReason());
        reservation_entity.setResidentId(residentService.getByStudentId(authentication.getName()).getId());
        reservation_entity.setStartReservation(reservation.getStartReservation());
        reservation_entity.setEndReservation(reservation.getEndReservation());

        if(reservation.getEndReservation().after(time))
            return new ResponseEntity<Long>(reservationService.create(reservation_entity).getId(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.REQUEST_TIMEOUT);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable Long id) {
        Reservation reservation = reservationService.getById(id);
        return reservation != null
                ? new ResponseEntity<>(reservation, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/is_active/{id}")
    public ResponseEntity<?> isActive(@PathVariable Long id) {
        Reservation reservation = reservationService.getById(id);
        return reservation != null
                ? new ResponseEntity<>(reservation.isActive(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/all")
    public ResponseEntity<List<Reservation>> getReservations() {
        final List<Reservation> reservations = reservationService.getAll();
        return reservations != null && !reservations.isEmpty()
                ? new ResponseEntity<>(reservations, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/by")
    public ResponseEntity<List<Reservation>> getReservationsBy(@RequestParam(value = "object_id", required = false) Long objectId,
                                                               @RequestParam(value = "student_id", required = false) String studentId) {
        List<Reservation> reservations = null;
        if(objectId != null && studentId != null)
            reservations =  reservationService.getAllByObjectIdAndResidentId(objectId, residentService.getByStudentId(studentId).getId());
        else if (objectId != null) {
            reservations = reservationService.getByObjectId(objectId);
        } else if (studentId != null) {
            reservations = reservationService.getAllByResidentId(residentService.getByStudentId(studentId).getId());
        }

        return reservations != null && !reservations.isEmpty()
                ? new ResponseEntity<>(reservations, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservation(Authentication authentication, @PathVariable Long id) {
        RoleType role = residentService.getRoleTypeByStudentId(authentication.getName());
        Reservation reservation = reservationService.getById(id);

        if(role.ordinal() >= RoleType.STUFF.ordinal()) {
            if (reservationService.getById(id) != null && reservationService.delete(id))
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else if(residentService.getById(reservation.getResidentId()).getStudentId().equals(authentication.getName()))
            if (reservationService.getById(id) != null && reservationService.delete(id))
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
