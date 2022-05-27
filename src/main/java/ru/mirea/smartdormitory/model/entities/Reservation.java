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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "object_id", insertable = false, updatable = false)
    @JsonIgnore
    private Object object;

    @Column(name = "object_id", nullable = false)
    private Long objectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_id", insertable = false, updatable = false)
    @JsonIgnore
    private Resident resident;

    @Column(name = "resident_id", nullable = false)
    private Long residentId;

    @Column(columnDefinition = "reason TEXT")
    private String reason;

    @Column(name = "start_reservation", nullable = false)
    private java.sql.Timestamp startReservation;

    @Column(name = "end_reservation", nullable = false)
    private java.sql.Timestamp endReservation;
}
