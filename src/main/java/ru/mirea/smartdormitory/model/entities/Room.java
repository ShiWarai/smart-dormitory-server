package ru.mirea.smartdormitory.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mirea.smartdormitory.model.types.RoomType;

import javax.persistence.*;

@Entity
@Table(name = "room")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @Column(name = "number")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long number;

    @Column(name = "floor", nullable = false)
    private String floor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", insertable = false, updatable = false)
    @JsonIgnore
    private RoomType type;

    @Column(name = "type_id", nullable = false)
    private Long type_id;
}
