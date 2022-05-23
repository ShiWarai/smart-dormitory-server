package ru.mirea.smartdormitory.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mirea.smartdormitory.model.entities.Object;
import ru.mirea.smartdormitory.model.entities.Resident;

@Repository
public interface IObjectRepository extends JpaRepository<Object, Long> {
    Object findObjectById(Long id);
}
