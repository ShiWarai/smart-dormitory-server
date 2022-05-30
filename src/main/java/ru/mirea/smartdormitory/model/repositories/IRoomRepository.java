package ru.mirea.smartdormitory.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mirea.smartdormitory.model.entities.Room;

import java.util.List;

@Repository
public interface IRoomRepository extends JpaRepository<Room, Long> {
    Room findByNumber(Long number);
    void deleteByNumber(Long number);
}
