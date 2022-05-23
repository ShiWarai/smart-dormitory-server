package ru.mirea.smartdormitory.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mirea.smartdormitory.model.entities.Resident;
import ru.mirea.smartdormitory.model.entities.Room;

@Repository
public interface IRoomRepository extends JpaRepository<Room, Long> {
    Room findRoomByNumber(Long number);
}
