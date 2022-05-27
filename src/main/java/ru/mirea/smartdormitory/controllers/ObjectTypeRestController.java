package ru.mirea.smartdormitory.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping(value="/object_types")
    public ResponseEntity<?> getObjectTypes() {
        final List<ObjectType> objectTypes = objectTypeService.getAll();
        return objectTypes != null && !objectTypes.isEmpty()
                ? new ResponseEntity<>(objectTypes, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
