package ru.mirea.smartdormitory.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.smartdormitory.model.entities.Reservation;
import ru.mirea.smartdormitory.model.repositories.IReservationRepository;

import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional
public class ReservationService extends AbstractService<Reservation, IReservationRepository>{
    private final IReservationRepository reservationRepository;

    @Autowired
    protected ReservationService(IReservationRepository repository) {
        super(repository);
        this.reservationRepository = repository;
    }

    @Override
    public Reservation update(Long id, Reservation entity) {
        findById(id);
        entity.setId(id);
        return create(entity);
    }

//    public List<Reservation> findByObjectIdAndIsExpired(Long objectId, boolean isExpired)
//    {
//        return reservationRepository.findAllByObjectIdAndIsExpired(objectId, isExpired);
//    }
//
//    public List<Reservation> findByExpiration(boolean isExpired)
//    {
//        return reservationRepository.findAllByIsExpired(isExpired);
//    }

    public List<Reservation> findAllByResidentId(Long id){
        return reservationRepository.findAllByResidentId(id);
    }

    public List<Reservation> findByObjectId(Long objectId)
    {
        return reservationRepository.findAllByObjectId(objectId);
    }

    public List<Reservation> findActiveByObjectId(Long objectId){
        Timestamp time = new Timestamp(System.currentTimeMillis());
        return reservationRepository.findAllByStartReservationIsBeforeAndEndReservationIsAfterAndObjectId(time, time, objectId);
    }

    public void deleteAllByObjectId(Long objectId) {
        reservationRepository.deleteAllByObjectId(objectId);
    }
}
