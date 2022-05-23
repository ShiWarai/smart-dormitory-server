package ru.mirea.smartdormitory.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mirea.smartdormitory.model.types.StatusType;

@Repository
public interface IStatusTypeRepository extends JpaRepository<StatusType, Long> {
}
