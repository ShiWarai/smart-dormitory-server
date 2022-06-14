package ru.mirea.smartdormitory.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.mirea.smartdormitory.model.entities.Object;
import ru.mirea.smartdormitory.services.*;

import java.util.List;

@RestController
@RequestMapping(value = "/object")
public class ObjectController {

    private final ObjectService objectService;
    private final ResidentService residentService;
    private final StatusTypeService statusTypeService;
    private final ObjectTypeService objectTypeService;
    private final ReservationService reservationService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    public ObjectController(ObjectService objectService,
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

    @PostMapping(value = "/", consumes = {"application/json"})
    @PreAuthorize("hasAuthority('STUFF')")
    public ResponseEntity<?> createObject(@RequestBody Object object) {
        return new ResponseEntity<Long>(objectService.create(object).getId(), HttpStatus.CREATED);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<Object> getObject(@PathVariable Long id) {
        Object object = objectService.findById(id);
        return object != null
                ? new ResponseEntity<>(object, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/all")
    public ResponseEntity<List<Object>> getObjects() {
        final List<Object> objects = objectService.getAll();
        return objects != null && !objects.isEmpty()
                ? new ResponseEntity<>(objects, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/status/{id}")
    public ResponseEntity<Long> getObjectStatus(@PathVariable Long id) {
        Object object = objectService.findById(id);
        return object != null
                ? new ResponseEntity<>(object.getStatusId(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value="/status/{id}/{status_id}")
    @PreAuthorize("hasAnyAuthority('STUFF', 'GUARD', 'COMMANDANT')")
    public ResponseEntity<?> setObjectStatus(@PathVariable Long id, @PathVariable Long status_id) {
        Object object = objectService.findById(id);

        if(object != null && statusTypeService.findById(status_id) != null) {
            object.setStatusId(status_id);
            objectService.update(id, object);

            // Call MQTT

            return object != null
                    ? new ResponseEntity<>(HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value= "/active_reservations/{id}")
    public ResponseEntity<?> getActiveReservations(@PathVariable Long id) {
        if(objectService.findById(id) == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        List<Long> activeReservations = reservationService.getAllActiveIdByObject(id);
        return new ResponseEntity<List<Long>>(activeReservations, HttpStatus.OK);
    }

    @GetMapping(value= "/reservations/{id}")
    public ResponseEntity<?> getReservations(@PathVariable Long id) {
        if(objectService.findById(id) == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        List<Long> reservations = reservationService.getAllIdByObject(id);
        return new ResponseEntity<List<Long>>(reservations, HttpStatus.OK);
    }

    @DeleteMapping(value= "/reservations/{id}")
    @PreAuthorize("hasAuthority('STUFF')")
    public ResponseEntity<?> deleteReservations(@PathVariable Long id) {
        if(objectService.findById(id) == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        reservationService.deleteAllByObjectId(id);
        return new ResponseEntity<>( HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('STUFF')")
    public ResponseEntity<Object> updateObject(@PathVariable Long id, @RequestBody Object object) {
        Object old_object = objectService.findById(id);
        if(old_object != null) {
            object.setId(id);
            return new ResponseEntity<Object>(objectService.update(object.getId(), object), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('STUFF')")
    public ResponseEntity<?> deleteObject(@PathVariable Long id) {
            if (objectService.findById(id) != null && objectService.delete(id))
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
