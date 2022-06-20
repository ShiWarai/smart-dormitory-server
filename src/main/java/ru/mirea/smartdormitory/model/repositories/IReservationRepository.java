package ru.mirea.smartdormitory.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mirea.smartdormitory.model.entities.Reservation;

import java.util.List;

@Repository
public interface IReservationRepository extends JpaRepository<Reservation, Long> {
    Reservation findReservationById(Long id);
//    List<Reservation> findAllByObjectIdAndIsExpired(Long id, boolean isExpired);
//    List<Reservation> findAllByIsExpired(boolean isExpired);
    List<Reservation> findAllByObjectId(Long id);
    List<Reservation> findAllByResidentId(Long id);
    List<Reservation> findAllByObjectIdAndResidentId(Long object_id, Long resident_id);
    List<Reservation> findAllByStartReservationIsBeforeAndEndReservationIsAfterAndObjectId(java.sql.Timestamp date1, java.sql.Timestamp date2, Long objectId);
    void deleteAllByObjectId(Long objectId);
}
