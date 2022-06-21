package ru.mirea.smartdormitory.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mirea.smartdormitory.model.entities.Object;
import ru.mirea.smartdormitory.model.entities.Resident;
import ru.mirea.smartdormitory.model.response_bodies.ObjectExtendedBody;
import ru.mirea.smartdormitory.services.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/tablet")
public class TabletController {

    private final ObjectService objectService;
    private final ResidentService residentService;
    private final ReservationService reservationService;

    @Autowired
    public TabletController(ObjectService objectService,
                            ResidentService residentService,
                            ReservationService reservationService) {
        this.objectService = objectService;
        this.residentService = residentService;
        this.reservationService = reservationService;
    }

    @GetMapping(value="/object/by")
    public ResponseEntity<List<ObjectExtendedBody>> getExtendedObjectsBy(Authentication authentication,
                                                                         @RequestParam(value = "room", required = false) Long roomNumber)
    {
        Resident resident = residentService.getByStudentId(authentication.getName());

        List<ObjectExtendedBody> extObjects = new ArrayList<ObjectExtendedBody>();

        List<Object> objects;
        if(roomNumber != null)
            objects = objectService.getAllByRoomNumber(roomNumber);
        else
            objects = objectService.getAll();

        for (Object object:objects) {
            ObjectExtendedBody extObject = new ObjectExtendedBody();

            extObject.setObject(object);
            extObject.setCanBeReserved(objectService.canBeReserved(object, reservationService));
            extObject.setResidentActiveReservations(reservationService.getAllActiveIdByObjectAndResidentId(object.getId(), resident.getId()));

            extObjects.add(extObject);
        }

        return extObjects != null && !extObjects.isEmpty()
                ? new ResponseEntity<>(extObjects, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
