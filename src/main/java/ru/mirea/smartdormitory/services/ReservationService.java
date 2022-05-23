package ru.mirea.smartdormitory.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.smartdormitory.model.entities.Reservation;
import ru.mirea.smartdormitory.model.repositories.IReservationRepository;

@Service
@Transactional
public class ReservationService extends AbstractService<Reservation, IReservationRepository>{
    private IReservationRepository reservationRepository;

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

    public Reservation getReservationById(Long userId) {
        return reservationRepository.findReservationById(userId);
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
}
