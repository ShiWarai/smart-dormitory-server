package ru.mirea.smartdormitory.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.mirea.smartdormitory.model.entities.Resident;
import ru.mirea.smartdormitory.model.repositories.IResidentRepository;
import ru.mirea.smartdormitory.model.types.RoleType;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class ResidentsTests {

    @Mock
    private IResidentRepository userRepository;
    private ResidentService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void setUp() {
        userService = new ResidentService(userRepository);
    }

    @Test
    void createResident(){

        String id = "2284856";

        Resident resident = new Resident();

        resident.setSurname("New");
        resident.setName("Resident");
        resident.setStudentId(id);
        resident.setRole(RoleType.STUDENT.name());
        resident.setPinCode(bCryptPasswordEncoder.encode("1111"));

        userService.create(resident);

        Mockito.when(userRepository.findResidentByStudentId(id)).thenReturn(resident);
        Resident foundUser = userService.findByStudentId(id);
        Assertions.assertEquals(resident, foundUser);
    }
}