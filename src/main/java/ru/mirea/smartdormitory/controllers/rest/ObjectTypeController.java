package ru.mirea.smartdormitory.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.mirea.smartdormitory.model.types.ObjectType;
import ru.mirea.smartdormitory.services.ObjectTypeService;

import java.util.List;

@RestController
@RequestMapping(value = "/object_type")
public class ObjectTypeController {

    private final ObjectTypeService objectTypeService;

    @Autowired
    public ObjectTypeController(ObjectTypeService objectTypeService) {
        this.objectTypeService = objectTypeService;
    }

    @GetMapping(value="/schedule/{id}")
    public ResponseEntity<?> getSchedule(@PathVariable Long id) {
        ObjectType objectType = objectTypeService.findById(id);
        return objectType != null
                ? new ResponseEntity<>(objectType.getSchedule(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value="schedule/{id}")
    @PreAuthorize("hasAuthority('COMMANDANT')")
    public ResponseEntity<?> setSchedule(@PathVariable Long id, @RequestBody String cron_schedule) {
        ObjectType objectType = objectTypeService.findById(id);

        if(objectType != null)
        {
            objectType.setSchedule(!cron_schedule.equals("null") ? cron_schedule : null);
            objectTypeService.update(objectType.getId(), objectType);

            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/all")
    public ResponseEntity<?> getObjectTypes() {
        final List<ObjectType> objectTypes = objectTypeService.getAll();
        return objectTypes != null && !objectTypes.isEmpty()
                ? new ResponseEntity<>(objectTypes, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
