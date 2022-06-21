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
    
    @PostMapping(value = "/", consumes = {"application/json"})
    @PreAuthorize("hasAnyAuthority('COMMANDANT', 'STUFF')")
    public ResponseEntity<?> createObjectType(@RequestBody ObjectType objectType) {
        return new ResponseEntity<Long>(objectTypeService.create(objectType).getId(), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('COMMANDANT', 'STUFF')")
    public ResponseEntity<ObjectType> updateObjectType(@PathVariable Long id, @RequestBody ObjectType objectType) {
        if(objectTypeService.getById(id) != null) {
            objectType.setId(id);
            return new ResponseEntity<>(objectTypeService.update(objectType.getId(), objectType), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/schedule/{id}")
    public ResponseEntity<?> getSchedule(@PathVariable Long id) {
        ObjectType objectType = objectTypeService.getById(id);
        return objectType != null
                ? new ResponseEntity<>(objectType.getSchedule(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value="/schedule/{id}")
    @PreAuthorize("hasAnyAuthority('COMMANDANT', 'STUFF')")
    public ResponseEntity<?> setSchedule(@PathVariable Long id, @RequestBody String cron_schedule) {
        ObjectType objectType = objectTypeService.getById(id);

        if(objectType != null)
        {
            objectType.setSchedule(!cron_schedule.equals("null") ? cron_schedule : null);
            objectTypeService.update(objectType.getId(), objectType);

            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/{id}")
    @PreAuthorize("hasAnyAuthority('COMMANDANT', 'STUFF')")
    public ResponseEntity<?> getObjectType(@PathVariable Long id) {
        ObjectType objectType = objectTypeService.getById(id);
        return objectType != null
                ? new ResponseEntity<>(objectType, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/all")
    @PreAuthorize("hasAnyAuthority('COMMANDANT', 'STUFF')")
    public ResponseEntity<?> getObjectTypes() {
        final List<ObjectType> objectTypes = objectTypeService.getAll();
        return objectTypes != null && !objectTypes.isEmpty()
                ? new ResponseEntity<>(objectTypes, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('COMMANDANT', 'STUFF')")
    public ResponseEntity<?> deleteObjectType(@PathVariable Long id) {
        if (objectTypeService.getById(id) != null && objectTypeService.delete(id))
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
