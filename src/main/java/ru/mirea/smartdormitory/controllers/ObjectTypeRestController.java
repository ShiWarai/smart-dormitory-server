package ru.mirea.smartdormitory.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.mirea.smartdormitory.model.entities.Object;
import ru.mirea.smartdormitory.model.types.ObjectType;
import ru.mirea.smartdormitory.services.ObjectTypeService;
import ru.mirea.smartdormitory.services.ReservationService;

import java.util.List;

@RestController
public class ObjectTypeRestController {

    private final ObjectTypeService objectTypeService;
    private final ReservationService reservationService;

    @Autowired
    public ObjectTypeRestController(ObjectTypeService objectTypeService,
                                    ReservationService reservationService) {
        this.objectTypeService = objectTypeService;
        this.reservationService = reservationService;
    }

    @GetMapping(value="/object_type/schedule/{id}")
    public ResponseEntity<?> getSchedule(@PathVariable Long id) {
        ObjectType objectType = objectTypeService.findById(id);
        return objectType != null
                ? new ResponseEntity<>(objectType.getSchedule(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value="/object_type/schedule/{id}")
    @PreAuthorize("hasAuthority('COMMANDANT')")
    public ResponseEntity<?> setSchedule(@PathVariable Long id, @RequestBody String cron_schedule) {
        ObjectType objectType = objectTypeService.findById(id);

        if(objectType != null)
        {
            objectType.setSchedule(cron_schedule);
            objectTypeService.update(objectType.getId(), objectType);

            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/object_types")
    public ResponseEntity<?> getObjectTypes() {
        final List<ObjectType> objectTypes = objectTypeService.getAll();
        return objectTypes != null && !objectTypes.isEmpty()
                ? new ResponseEntity<>(objectTypes, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
