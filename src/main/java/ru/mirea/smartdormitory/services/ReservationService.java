package ru.mirea.smartdormitory.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.smartdormitory.model.entities.Reservation;
import ru.mirea.smartdormitory.repositories.IReservationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ReservationService extends AbstractService<Reservation, IReservationRepository>{

    @Autowired
    protected ReservationService(IReservationRepository repository) {
        super(repository);
    }

    @Override
    public Reservation update(Long id, Reservation entity) {
        findById(id);
        entity.setId(id);
        return create(entity);
    }

    public List<Reservation> findAllByResidentId(Long id)
    {
        return repository.findAllByResidentId(id);
    }

    public List<Reservation> findByObjectId(Long objectId)
    {
        return repository.findAllByObjectId(objectId);
    }

    public List<Reservation> findAllByObjectIdAndResidentId(Long object_id, Long resident_id)
    {
        return repository.findAllByObjectIdAndResidentId(object_id, resident_id);
    }

    public void deleteAllByObjectId(Long objectId) {
        repository.deleteAllByObjectId(objectId);
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
