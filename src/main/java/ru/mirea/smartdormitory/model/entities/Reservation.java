package ru.mirea.smartdormitory.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "reservation")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "object_id", nullable = false)
    private Long objectId;

    @Column(name = "resident_id", nullable = false)
    private Long residentId;

    @Column(columnDefinition = "reason TEXT")
    private String reason;

    @Column(name = "start_reservation", nullable = false)
    private java.sql.Timestamp startReservation;

    @Column(name = "end_reservation", nullable = false)
    private java.sql.Timestamp endReservation;

//    @Column(columnDefinition = "is_expired boolean default false not null")
//    private boolean isExpired;
}
