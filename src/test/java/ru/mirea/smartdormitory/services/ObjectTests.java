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
import ru.mirea.smartdormitory.model.entities.Object;
import ru.mirea.smartdormitory.model.repositories.IObjectRepository;
import ru.mirea.smartdormitory.model.types.ObjectType;

@ExtendWith(MockitoExtension.class)
public class ObjectTests {

    @Mock
    private IObjectRepository objectRepository;
    private ObjectService objectService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void setUp() {
        objectService = new ObjectService(objectRepository);
    }

    @Test
    void createObject(){
        Object object = new Object(123L, "test", "test", new ObjectType(), 1L, 1L, 1L);;

        objectService.create(object);

        ArgumentCaptor<Object> objectArgumentCaptor = ArgumentCaptor.forClass(Object.class);
        Mockito.verify(objectRepository).save(objectArgumentCaptor.capture());

        Object objectCaptorValue = objectArgumentCaptor.getValue();
        Assertions.assertThat(objectCaptorValue).isEqualTo(object);
    }
}