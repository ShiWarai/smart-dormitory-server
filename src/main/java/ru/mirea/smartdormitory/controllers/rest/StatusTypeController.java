package ru.mirea.smartdormitory.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mirea.smartdormitory.model.types.StatusType;
import ru.mirea.smartdormitory.services.StatusTypeService;

import java.util.List;

@RestController
public class StatusTypeController {

    private final StatusTypeService statusTypeService;

    @Autowired
    public StatusTypeController(StatusTypeService statusTypeService) {
        this.statusTypeService = statusTypeService;
    }

    @GetMapping(value="/status_types")
    public ResponseEntity<?> getStatusTypes() {
        final List<StatusType> statusTypes = statusTypeService.getAll();
        return statusTypes != null && !statusTypes.isEmpty()
                ? new ResponseEntity<>(statusTypes, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
