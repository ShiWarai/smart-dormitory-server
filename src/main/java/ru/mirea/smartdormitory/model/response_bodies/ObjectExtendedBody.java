package ru.mirea.smartdormitory.model.response_bodies;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.mirea.smartdormitory.model.entities.Object;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ObjectExtendedBody {
    Object object;
    boolean canBeReserved;
    List<Long> residentActiveReservations;
}
