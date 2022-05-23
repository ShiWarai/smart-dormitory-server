package ru.mirea.smartdormitory.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mirea.smartdormitory.model.entities.Resident;

@Repository
public interface IResidentRepository extends JpaRepository<Resident, Long> {
    Resident findResidentByStudentId(String student_id);
    Resident findResidentById(Long id);
}
