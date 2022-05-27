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
import ru.mirea.smartdormitory.model.repositories.IObjectTypeRepository;
import ru.mirea.smartdormitory.model.types.ObjectType;


@ExtendWith(MockitoExtension.class)
public class ObjectTypeTests {

    @Mock
    private IObjectTypeRepository objectTypeRepository;
    private ObjectTypeService objectTypeService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void setUp() {
        objectTypeService = new ObjectTypeService(objectTypeRepository);
    }

    @Test
    void createObjectType(){
        ObjectType objectType = new ObjectType();
        objectType.setSchedule("* * * * * *");

        objectTypeService.create(objectType);

        ArgumentCaptor<ObjectType> objectTypeArgumentCaptor = ArgumentCaptor.forClass(ObjectType.class);
        Mockito.verify(objectTypeRepository).save(objectTypeArgumentCaptor.capture());

        ObjectType objectTypeCaptorValue = objectTypeArgumentCaptor.getValue();
        Assertions.assertThat(objectTypeCaptorValue).isEqualTo(objectType);
    }
}