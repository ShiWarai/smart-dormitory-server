package ru.mirea.smartdormitory.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mirea.smartdormitory.model.types.ObjectType;
import ru.mirea.smartdormitory.model.types.StatusType;

import javax.persistence.*;

@Entity
@Table(name = "object")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Object {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 64, nullable = false)
    private String name;

    @Column(columnDefinition = "description TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", insertable = false, updatable = false)
    @JsonIgnore
    private ObjectType type;

    @Column(name = "type_id", nullable = false)
    private Long typeId;

    @Column(columnDefinition = "status_id INTEGER default 0 not null")
    private Long statusId;

    @Column(name = "room_number", nullable = false)
    private Long roomNumber;
}
