package ru.mirea.smartdormitory.model.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "object_type")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ObjectType {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "reservation_limit", nullable = true)
    private Long reservationLimit;

    @Column(name = "schedule")
    private String schedule;
}
