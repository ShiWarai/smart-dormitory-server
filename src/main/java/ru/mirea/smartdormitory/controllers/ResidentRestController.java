package ru.mirea.smartdormitory.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.mirea.smartdormitory.model.entities.Resident;
import ru.mirea.smartdormitory.model.types.RoleType;
import ru.mirea.smartdormitory.services.ResidentService;

import java.util.List;

@RestController
public class ResidentRestController {

    private final ResidentService service;

    @Autowired
    public ResidentRestController(ResidentService service) {
        this.service = service;
    }

    @PostMapping(value = "/add_resident", consumes = {"application/json"})
    public ResponseEntity<?> createResident(Authentication authentication, @RequestBody Resident resident) {
        String userRole = service.findResidentByStudentId(authentication.getName()).getRole();
        if(userRole.equals(RoleType.COMMANDANT.name())) {
            return new ResponseEntity<Long>(service.create(resident).getId(), HttpStatus.CREATED);
        }
        else
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping(value="/get_residents")
    public ResponseEntity<List<Resident>> readResidents() {
        final List<Resident> residents = service.getAll();
        return residents != null && !residents.isEmpty()
                ? new ResponseEntity<>(residents, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/get_resident/{id}")
    public ResponseEntity<Resident> getResidentBy(@PathVariable Long id) {
        Resident resident = service.findById(id);
        return resident != null
                ? new ResponseEntity<>(resident, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
