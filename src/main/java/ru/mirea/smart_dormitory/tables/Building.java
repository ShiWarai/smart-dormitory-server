package ru.mirea.smart_dormitory.tables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "building_relationship")
@Setter
@Getter
@NoArgsConstructor
public class Building {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private String date;

    @Column(name = "type")
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_address", insertable = false, updatable = false)
    @JsonIgnore
    private Address address;

    @Column(name = "id_address")
    private Long id_address;

    @Override
    public String toString() {
        return "Building{" +
                "creationDate='" + date + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
