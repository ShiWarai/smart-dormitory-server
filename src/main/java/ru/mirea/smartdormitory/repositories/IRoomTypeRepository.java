package ru.mirea.smartdormitory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mirea.smartdormitory.model.types.RoomType;

@Repository
public interface IRoomTypeRepository extends JpaRepository<RoomType, Long> {
    RoomType findRoomTypeByName(String name);
}
