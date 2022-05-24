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
import ru.mirea.smartdormitory.model.types.StatusType;
import ru.mirea.smartdormitory.services.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ObjectRestController {

    private final ObjectService objectService;
    private final ResidentService residentService;
    private final StatusTypeService statusTypeService;
    private final ObjectTypeService objectTypeService;
    private final ReservationService reservationService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    public ObjectRestController(ObjectService objectService,
                                ResidentService residentService,
                                StatusTypeService statusTypeService,
                                ObjectTypeService objectTypeService,
                                ReservationService reservationService) {
        this.objectService = objectService;
        this.residentService = residentService;
        this.statusTypeService = statusTypeService;
        this.objectTypeService = objectTypeService;
        this.reservationService = reservationService;
    }

    @PostMapping(value = "/object/add", consumes = {"application/json"})
    public ResponseEntity<?> createObject(Authentication authentication, @RequestBody Object object) {
        RoleType role = residentService.getResidentRoleByStudentId(authentication.getName());
        if(role == RoleType.STUFF) {
            return new ResponseEntity<Long>(objectService.create(object).getId(), HttpStatus.CREATED);
        }
        else
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping(value="/object/get/{id}")
    public ResponseEntity<Object> getObject(@PathVariable Long id) {
        Object object = objectService.findById(id);
        return object != null
                ? new ResponseEntity<>(object, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/object/get_all")
    public ResponseEntity<List<Object>> getObjects() {
        final List<Object> objects = objectService.getAll();
        return objects != null && !objects.isEmpty()
                ? new ResponseEntity<>(objects, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/object/get_status/{id}")
    public ResponseEntity<Long> getObjectStatus(@PathVariable Long id) {
        Object object = objectService.findById(id);
        return object != null
                ? new ResponseEntity<>(object.getStatusId(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value="/object/set_status/{id}/{status_id}")
    public ResponseEntity<?> setObjectStatus(Authentication authentication, @PathVariable Long id, @PathVariable Long status_id) {
        RoleType role = residentService.getResidentRoleByStudentId(authentication.getName());
        if(role.ordinal() >= RoleType.STUFF.ordinal()) {
            if(statusTypeService.findById(status_id) != null) {
                Object object = objectService.findById(id);
                object.setStatusId(status_id);
                objectService.update(id, object);
                return object != null
                        ? new ResponseEntity<>(HttpStatus.OK)
                        : new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    private List<Long> getActiveByObject(Long objectId) {
        List<Long> currentReservationIds = new ArrayList<Long>();

        List<Reservation> activeReservations = reservationService.findActiveByObjectId(objectId);

        for (Reservation reservation : activeReservations) {
            currentReservationIds.add(reservation.getId());
        }

        return currentReservationIds;
    }

    private List<Long> getByObject(Long objectId) {
        List<Long> currentReservationIds = new ArrayList<Long>();

        List<Reservation> reservations = reservationService.findByObjectId(objectId);

        for (Reservation reservation : reservations) {
            currentReservationIds.add(reservation.getId());
        }

        return currentReservationIds;
    }

    @GetMapping(value= "/object/get_active_reservations/{id}")
    public ResponseEntity<?> getActiveReservations(@PathVariable Long id) {
        if(objectService.findById(id) == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        List<Long> activeReservations = getActiveByObject(id);
        return new ResponseEntity<List<Long>>(activeReservations, HttpStatus.OK);
    }

    @GetMapping(value= "/object/get_all_reservations/{id}")
    public ResponseEntity<?> getReservations(@PathVariable Long id) {
        if(objectService.findById(id) == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        List<Long> reservations = getByObject(id);
        return new ResponseEntity<List<Long>>(reservations, HttpStatus.OK);
    }

    @DeleteMapping(value= "/object/delete_reservations/{id}")
    public ResponseEntity<?> deleteReservations(@PathVariable Long id) {
        if(objectService.findById(id) == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        reservationService.deleteAllByObjectId(id);
        return new ResponseEntity<>( HttpStatus.OK);
    }

    @PutMapping("/object/update/{id}")
    public ResponseEntity<Object> updateObject(Authentication authentication, @PathVariable Long id, @RequestBody Object object) {
        RoleType role = residentService.getResidentRoleByStudentId(authentication.getName());

        if(role == RoleType.STUFF) {
            Object old_object = objectService.findById(id);
            if(old_object != null) {
                object.setId(id);
                return new ResponseEntity<Object>(objectService.create(object), HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/object/delete/{id}")
    public ResponseEntity<?> deleteObject(Authentication authentication, @PathVariable Long id) {
        RoleType role = residentService.getResidentRoleByStudentId(authentication.getName());
        if(role == RoleType.STUFF) {
            if (objectService.findById(id) != null && objectService.delete(id))
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping(value="/object_types")
    public ResponseEntity<?> getObjectTypes() {
        final List<ObjectType> objectTypes = objectTypeService.getAll();
        return objectTypes != null && !objectTypes.isEmpty()
                ? new ResponseEntity<>(objectTypes, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/status_types")
    public ResponseEntity<?> getStatusTypes() {
        final List<StatusType> statusTypes = statusTypeService.getAll();
        return statusTypes != null && !statusTypes.isEmpty()
                ? new ResponseEntity<>(statusTypes, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
