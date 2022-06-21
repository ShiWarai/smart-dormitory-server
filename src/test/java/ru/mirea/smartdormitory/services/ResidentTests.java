package ru.mirea.smartdormitory.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.mirea.smartdormitory.model.entities.Resident;
import ru.mirea.smartdormitory.repositories.IResidentRepository;
import ru.mirea.smartdormitory.model.types.RoleType;

@ExtendWith(MockitoExtension.class)
public class ResidentTests {

    @Mock
    private IResidentRepository residentRepository;
    private ResidentService residentService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void setUp() {
        residentService = new ResidentService(residentRepository);
    }

    @Test
    void createResident(){

        Resident resident = new Resident();

        resident.setSurname("New");
        resident.setName("Resident");
        resident.setStudentId("2284856");
        resident.setRole(RoleType.STUDENT.name());
        resident.setPinCode(encoder.encode("1111"));
        residentService.create(resident);

        ArgumentCaptor<Resident> residentArgumentCaptor = ArgumentCaptor.forClass(Resident.class);
        Mockito.verify(residentRepository).save(residentArgumentCaptor.capture());

        Resident residentCaptorValue = residentArgumentCaptor.getValue();
        Assertions.assertThat(residentCaptorValue).isEqualTo(resident);
    }
}