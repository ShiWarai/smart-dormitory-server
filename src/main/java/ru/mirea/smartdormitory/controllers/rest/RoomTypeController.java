package ru.mirea.smartdormitory.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.mirea.smartdormitory.model.types.RoomType;
import ru.mirea.smartdormitory.services.RoomTypeService;

import java.util.List;

@RestController
@RequestMapping(value = "/room_type")
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    @Autowired
    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

//    @PostMapping(value = "/", consumes = {"application/json"})
//    @PreAuthorize("hasAuthority('COMMANDANT')")
//    public ResponseEntity<?> createRoomType(@RequestBody RoomType roomType) {
//        return new ResponseEntity<Long>(roomTypeService.create(roomType).getId(), HttpStatus.CREATED);
//    }

    @GetMapping(value="/{id}")
    public ResponseEntity<RoomType> getRoomType(@PathVariable Long id) {
        RoomType roomType = roomTypeService.getById(id);
        return roomType != null
                ? new ResponseEntity<>(roomType, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/all")
    public ResponseEntity<?> getRoomTypes() {
        final List<RoomType> roomTypes = roomTypeService.getAll();
        return roomTypes != null && !roomTypes.isEmpty()
                ? new ResponseEntity<>(roomTypes, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('COMMANDANT')")
    public ResponseEntity<RoomType> updateRoomType(@PathVariable Long id, @RequestBody RoomType roomType) {
        if(roomTypeService.getById(id) != null) {
            roomType.setId(id);
            return new ResponseEntity<>(roomTypeService.update(roomType.getId(), roomType), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('COMMANDANT')")
//    public ResponseEntity<?> deleteRoomType(@PathVariable Long id) {
//        if (roomTypeService.findById(id) != null && roomTypeService.delete(id))
//            return new ResponseEntity<>(HttpStatus.OK);
//        else
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
}
