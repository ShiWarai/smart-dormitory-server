package ru.mirea.smartdormitory.repositories;

import lombok.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.smartdormitory.model.entities.Resident;

import java.sql.Date;

@Repository
public interface IResidentRepository extends JpaRepository<Resident, Long> {
    Resident findResidentByStudentId(String student_id);

    @Modifying
    @Query("update Resident c set c.surname = ?2, c.name = ?3, c.patronymic = ?4, c.birthdate = ?5, c.pinCode = ?6, c.roomNumber = ?7,  c.role = ?8 where c.studentId = ?1")
    Integer updatePropertiesByStudentId(String studentId, String surname, String name, String patronymic, Date birthdate, String pinCode, Long roomNumber, String role);
}
