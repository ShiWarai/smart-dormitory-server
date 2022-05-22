package ru.mirea.smart_dormitory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mirea.smart_dormitory.tables.Address;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAllByText(String text);
    List<Address> findAllByZip(String zip);
    List<Address> findAllByTextAndZip(String text, String zip);
}
