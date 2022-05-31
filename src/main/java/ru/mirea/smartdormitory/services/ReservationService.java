package ru.mirea.smartdormitory.services;

import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.smartdormitory.model.entities.Reservation;
import ru.mirea.smartdormitory.model.repositories.IReservationRepository;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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

    public List<Reservation> findAllByResidentId(Long id){
        return reservationRepository.findAllByResidentId(id);
    }

    public List<Reservation> findByObjectId(Long objectId)
    {
        return reservationRepository.findAllByObjectId(objectId);
    }

    public void deleteAllByObjectId(Long objectId) {
        reservationRepository.deleteAllByObjectId(objectId);
    }

    public List<Long> getAllActiveIdByObject(Long objectId) {
        List<Long> currentReservationIds = new ArrayList<Long>();

        List<Reservation> activeReservations = this.findByObjectId(objectId);

        for (Reservation reservation : activeReservations) {
            if(reservation.isActive())
                currentReservationIds.add(reservation.getId());
        }

        return currentReservationIds;
    }

    public List<Long> getAllIdByObject(Long objectId) {
        List<Long> currentReservationIds = new ArrayList<Long>();

        List<Reservation> reservations = this.findByObjectId(objectId);

        for (Reservation reservation : reservations) {
            currentReservationIds.add(reservation.getId());
        }

        return currentReservationIds;
    }
}
