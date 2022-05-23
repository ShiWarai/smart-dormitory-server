package ru.mirea.smartdormitory.model.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "status_type")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StatusType {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", unique = true, nullable = true)
    private String name;
}
