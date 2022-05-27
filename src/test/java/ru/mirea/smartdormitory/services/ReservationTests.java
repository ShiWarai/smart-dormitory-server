package ru.mirea.smartdormitory.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.mirea.smartdormitory.model.entities.Object;
import ru.mirea.smartdormitory.model.entities.Reservation;
import ru.mirea.smartdormitory.model.entities.Resident;
import ru.mirea.smartdormitory.model.repositories.IReservationRepository;

import java.sql.Timestamp;

@ExtendWith(MockitoExtension.class)
public class ReservationTests {

    @Mock
    private IReservationRepository reservationRepository;
    private ReservationService reservationService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void setUp() {
        reservationService = new ReservationService(reservationRepository);
    }

    @Test
    void createReservation(){
        Timestamp time0 = new Timestamp(System.currentTimeMillis() - 100000);
        Timestamp time1 = new Timestamp(System.currentTimeMillis() + 100000);

        Reservation reservation = new Reservation(123L, new Object(), 1L, new Resident(), 1L, "test", time0, time1);

        reservationService.create(reservation);

        ArgumentCaptor<Reservation> reservationArgumentCaptor = ArgumentCaptor.forClass(Reservation.class);
        Mockito.verify(reservationRepository).save(reservationArgumentCaptor.capture());

        Reservation reservationCaptorValue = reservationArgumentCaptor.getValue();
        Assertions.assertThat(reservationCaptorValue).isEqualTo(reservation);
    }
}