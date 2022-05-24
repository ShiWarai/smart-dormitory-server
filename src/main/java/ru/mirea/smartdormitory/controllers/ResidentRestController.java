package ru.mirea.smartdormitory.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.mirea.smartdormitory.model.entities.Resident;
import ru.mirea.smartdormitory.model.types.RoleType;
import ru.mirea.smartdormitory.services.ResidentService;

import java.util.List;

@RestController
public class ResidentRestController {

    private final ResidentService residentService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    public ResidentRestController(ResidentService service) {
        this.residentService = service;
    }

    @PostMapping(value = "/resident/add", consumes = {"application/json"})
    public ResponseEntity<?> createResident(Authentication authentication, @RequestBody Resident resident) {
        // Регистрация разрешена только коменданту и охране (для гостей)
        RoleType role = residentService.getResidentRoleByStudentId(authentication.getName());
        if(role == RoleType.COMMANDANT || role == RoleType.GUARD) {
            resident.setPinCode(encoder.encode(resident.getPinCode()));
            return new ResponseEntity<Long>(residentService.create(resident).getId(), HttpStatus.CREATED);
        }
        else
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping(value="/resident/get/{student_id}")
    public ResponseEntity<Resident> getResidentBy(Authentication authentication, @PathVariable String student_id) {
        RoleType role = residentService.getResidentRoleByStudentId(authentication.getName());
        if(role == RoleType.COMMANDANT || role == RoleType.GUARD || authentication.getName().equals(student_id))
        {
            Resident resident = residentService.findResidentByStudentId(student_id);
            return resident != null
                    ? new ResponseEntity<>(resident, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping(value="/resident/get_all")
    public ResponseEntity<List<Resident>> readResidents(Authentication authentication) {
        RoleType role = residentService.getResidentRoleByStudentId(authentication.getName());
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

    @PutMapping("/resident/update")
    public ResponseEntity<Resident> updateResident(Authentication authentication, @RequestBody Resident resident) {
        RoleType role = residentService.getResidentRoleByStudentId(authentication.getName());

        if(role == RoleType.COMMANDANT || authentication.getName().equals(resident.getStudentId())) {
            Resident old_resident = residentService.findResidentByStudentId(resident.getStudentId());
            if(old_resident != null) {
                resident.setId(old_resident.getId());
                resident.setStudentId(old_resident.getStudentId());
                resident.setPinCode(encoder.encode(resident.getPinCode()));
                return new ResponseEntity<Resident>(residentService.create(resident), HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/resident/delete/{student_id}")
    public ResponseEntity<?> deleteResident(Authentication authentication, @PathVariable String student_id) {
        RoleType role = residentService.getResidentRoleByStudentId(authentication.getName());

        if((role == RoleType.COMMANDANT) || (authentication.getName().equals(student_id)))
            if(residentService.findResidentByStudentId(student_id) != null && residentService.delete(residentService.findResidentByStudentId(student_id).getId()))
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
