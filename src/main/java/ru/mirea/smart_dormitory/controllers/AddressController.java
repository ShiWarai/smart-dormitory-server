package ru.mirea.smart_dormitory.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mirea.smart_dormitory.services.AddressService;
import ru.mirea.smart_dormitory.services.TableService;
import ru.mirea.smart_dormitory.tables.Address;

import java.util.List;
import java.util.Optional;

@RestController
public class AddressController {

    private final TableService<Address> serviceTableService;

    @Autowired
    public AddressController(TableService<Address> serviceTableService) {
        this.serviceTableService = serviceTableService;
    }

    @PostMapping(value = "/add_address", consumes = {"application/json"})
    public ResponseEntity<?> createAddress(@RequestBody Address address) {
        serviceTableService.createEntity(address);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value="/get_addresses")
    public ResponseEntity<List<Address>> readAddresses() {
        final List<Address> addresses = serviceTableService.readAllEntity();
        return addresses != null && !addresses.isEmpty()
                ? new ResponseEntity<>(addresses, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/get_addresses/by")
    public ResponseEntity<List<Address>> getAddressBy(@RequestParam(value = "text", required = false) String text,
                                                      @RequestParam(value = "zip", required = false) String zip) {
        List<Address> addresses = ((AddressService) serviceTableService).filterBy(text, zip);
        return addresses != null && !addresses.isEmpty()
                ? new ResponseEntity<>(addresses, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value="/delete_addresses")
    public ResponseEntity<?> deleteBuildings() {
        final boolean isDeleted = serviceTableService.deleteAllEntity();
        return isDeleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

}
