package ru.mirea.smartdormitory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mirea.smartdormitory.model.entities.Object;

import java.util.List;

@Repository
public interface IObjectRepository extends JpaRepository<Object, Long> {
    Object findObjectById(Long id);
    List<Object> findAllByRoomNumber(long roomNumber);
}
