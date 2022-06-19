package ru.mirea.smartdormitory.model.request_bodies;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationBody {
        private Long objectId;

        private String reason;

        private java.sql.Timestamp startReservation;

        private java.sql.Timestamp endReservation;
}
