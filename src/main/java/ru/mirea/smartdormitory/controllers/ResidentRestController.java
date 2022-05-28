package ru.mirea.smartdormitory.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.mirea.smartdormitory.model.entities.Resident;
import ru.mirea.smartdormitory.model.types.RoleType;
import ru.mirea.smartdormitory.services.ResidentService;

import java.util.List;

@RestController
@RequestMapping(value = "/resident")
public class ResidentRestController {

    private final ResidentService residentService;

    @Autowired
    public ResidentRestController(ResidentService service) {
        this.residentService = service;
    }

    @PostMapping(value = "/", consumes = {"application/json"})
    @PreAuthorize("hasAnyAuthority('COMMANDANT')")
    public ResponseEntity<?> createResident(@RequestBody Resident resident) {
        if(residentService.findByStudentId(resident.getStudentId()) == null)
            return new ResponseEntity<Long>(residentService.create(resident).getId(), HttpStatus.CREATED);
        else
            return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping(value="/{student_id}")
    public ResponseEntity<Resident> getResident(@PathVariable String student_id) {
        Resident resident = residentService.findByStudentId(student_id);
        return resident != null
                ? new ResponseEntity<>(resident, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/all")
    public ResponseEntity<List<Resident>> getResidents(Authentication authentication) {
        RoleType role = residentService.getRoleTypeByStudentId(authentication.getName());
        if(role == RoleType.COMMANDANT || role == RoleType.GUARD)
        {
            final List<Resident> residents = residentService.getAll();
            return residents != null && !residents.isEmpty()
                    ? new ResponseEntity<>(residents, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping("/{student_id}")
    public ResponseEntity<Resident> updateResident(Authentication authentication, @PathVariable String student_id, @RequestBody Resident resident) {
        RoleType role = residentService.getRoleTypeByStudentId(authentication.getName());

        if(role == RoleType.COMMANDANT || authentication.getName().equals(resident.getStudentId())) {
            Resident old_resident = residentService.findByStudentId(student_id);

            if(old_resident != null) {

                if(role != RoleType.COMMANDANT)
                    resident.setRole(old_resident.getRole());

                return new ResponseEntity<Resident>(residentService.update(old_resident.getId(), resident), HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{student_id}")
    @PreAuthorize("hasAnyAuthority('COMMANDANT')")
    public ResponseEntity<?> deleteResident(@PathVariable String student_id) {
        if (residentService.findByStudentId(student_id) != null && residentService.delete(residentService.findByStudentId(student_id).getId()))
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
