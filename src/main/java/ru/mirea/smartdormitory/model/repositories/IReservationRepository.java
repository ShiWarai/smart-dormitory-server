package ru.mirea.smartdormitory.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mirea.smartdormitory.model.entities.Object;
import ru.mirea.smartdormitory.model.entities.Reservation;

@Repository
public interface IReservationRepository extends JpaRepository<Reservation, Long> {
    Reservation findReservationById(Long id);
}
