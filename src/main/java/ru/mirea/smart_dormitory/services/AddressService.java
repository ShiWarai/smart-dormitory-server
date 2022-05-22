package ru.mirea.smart_dormitory.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.smart_dormitory.repositories.AddressRepository;
import ru.mirea.smart_dormitory.tables.Address;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AddressService implements TableService<Address>{
    private AddressRepository addressRepository;
    @Autowired
    AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public void createEntity(Address address) {
        addressRepository.save(address);
    }


    public List<Address> readAllEntity() {
        return addressRepository.findAll();
    }

    public boolean deleteAllEntity() {
        addressRepository.deleteAll();
        return true;
    }

    public List<Address> filterBy(String text, String zip) {
        if(text != null && zip != null)
            return addressRepository.findAllByTextAndZip(text, zip);
        else if (text != null) {
            return addressRepository.findAllByText(text);
        } else if (zip != null) {
            return addressRepository.findAllByZip(zip);
        }

        return null;
    }
}
