package ru.mirea.smartdormitory.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.mirea.smartdormitory.model.entities.Room;
import ru.mirea.smartdormitory.services.*;

import java.util.List;

@RestController
@RequestMapping(value = "/room")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping(value = "/", consumes = {"application/json"})
    @PreAuthorize("hasAuthority('COMMANDANT')")
    public ResponseEntity<?> createRoom(@RequestBody Room room) {
        return new ResponseEntity<Long>(roomService.create(room).getNumber(), HttpStatus.CREATED);
    }

    @GetMapping(value="/{number}")
    public ResponseEntity<Room> getRoom(@PathVariable Long number) {
        Room room = roomService.findById(number);
        return room != null
                ? new ResponseEntity<>(room, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/all")
    public ResponseEntity<List<Room>> getRooms() {
        final List<Room> rooms = roomService.getAll();
        return rooms != null && !rooms.isEmpty()
                ? new ResponseEntity<>(rooms, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{number}")
    @PreAuthorize("hasAuthority('COMMANDANT')")
    public ResponseEntity<Room> updateRoom(@PathVariable Long number, @RequestBody Room room) {
        if(roomService.findById(number) != null) {
            room.setNumber(number);
            return new ResponseEntity<Room>(roomService.update(room.getNumber(), room), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{number}")
    @PreAuthorize("hasAuthority('COMMANDANT')")
    public ResponseEntity<?> deleteRoom(@PathVariable Long number) {
            if (roomService.findById(number) != null && roomService.delete(number))
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
