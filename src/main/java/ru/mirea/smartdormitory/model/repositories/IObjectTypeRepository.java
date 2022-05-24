package ru.mirea.smartdormitory.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mirea.smartdormitory.model.types.ObjectType;

@Repository
public interface IObjectTypeRepository extends JpaRepository<ObjectType, Long> {
}
