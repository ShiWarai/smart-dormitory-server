package ru.mirea.smartdormitory.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.mirea.smartdormitory.model.entities.Object;
import ru.mirea.smartdormitory.model.types.ObjectType;
import ru.mirea.smartdormitory.model.types.RoleType;
import ru.mirea.smartdormitory.model.types.StatusType;
import ru.mirea.smartdormitory.services.ObjectService;
import ru.mirea.smartdormitory.services.ObjectTypeService;
import ru.mirea.smartdormitory.services.ResidentService;
import ru.mirea.smartdormitory.services.StatusTypeService;

import java.util.List;

@RestController
public class ObjectRestController {

    private final ObjectService objectService;
    private final ResidentService residentService;
    private final StatusTypeService statusTypeService;
    private final ObjectTypeService objectTypeService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    public ObjectRestController(ObjectService objectService,
                                ResidentService residentService,
                                StatusTypeService statusTypeService,
                                ObjectTypeService objectTypeService) {
        this.objectService = objectService;
        this.residentService = residentService;
        this.statusTypeService = statusTypeService;
        this.objectTypeService = objectTypeService;
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
        if(role == RoleType.STUFF || role == RoleType.GUARD || role == RoleType.COMMANDANT) {
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
    public ResponseEntity<List<ObjectType>> getObjectTypes() {
        final List<ObjectType> objectTypes = objectTypeService.getAll();
        return objectTypes != null && !objectTypes.isEmpty()
                ? new ResponseEntity<>(objectTypes, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/status_types")
    public ResponseEntity<List<StatusType>> getStatusTypes() {
        final List<StatusType> statusTypes = statusTypeService.getAll();
        return statusTypes != null && !statusTypes.isEmpty()
                ? new ResponseEntity<>(statusTypes, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
