package ru.mirea.smartdormitory.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mirea.smartdormitory.model.types.ObjectType;

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

    @Column(name = "object_id")
    private Long objectId;

    @Column(name = "resident_ id")
    private Long residentId;

    @Column(name = "reason")
    private String reason;

    @Column(name = "start_reservation")
    private java.sql.Timestamp startReservation;

    @Column(name = "end_reservation")
    private java.sql.Timestamp endReservation;
}
