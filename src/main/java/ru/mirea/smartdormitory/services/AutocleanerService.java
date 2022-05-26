package ru.mirea.smartdormitory.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.mirea.smartdormitory.model.entities.Reservation;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@ManagedResource(description = "Autocleaner service")
@Slf4j
public class AutocleanerService {

    final static int maxDiff = 3;

    @Autowired
    private ReservationService reservationService;

    
    @Scheduled(cron = "0 0 0 * * ?")
    @ManagedOperation(description = "Clear outdated reservations")
    public void backupFromDatabase() {
        log.info("Scheduler task is started");

        Timestamp time = new Timestamp(System.currentTimeMillis());

        List<Reservation> reservations =  reservationService.getAll();
        for (Reservation reservation : reservations) {
            if(reservation.getEndReservation().before(time)) {
                Long diff = Math.abs(time.getTime() - reservation.getEndReservation().getTime());
                if(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) > maxDiff)
                    reservationService.delete(reservation.getId());
            }
        }

        log.info("Scheduler task is ended");
    }
}
