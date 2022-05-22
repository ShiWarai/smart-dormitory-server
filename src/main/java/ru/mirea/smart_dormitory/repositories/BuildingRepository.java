package ru.mirea.smart_dormitory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mirea.smart_dormitory.tables.Building;

import java.util.List;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {
    List<Building> findAllByDate(String date);
    List<Building> findAllByType(String type);
    List<Building> findAllByDateAndType(String date, String type);
}
