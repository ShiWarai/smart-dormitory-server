package ru.mirea.smartdormitory.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mirea.smartdormitory.model.types.ObjectType;
import ru.mirea.smartdormitory.model.types.RoomType;

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

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", insertable = false, updatable = false)
    @JsonIgnore
    private ObjectType type;

    @Column(name = "type_id")
    private Long type_id;

    @Column(name = "status_id")
    private Long status_id;
}
