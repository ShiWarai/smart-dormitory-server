package ru.mirea.smartdormitory.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mirea.smartdormitory.repositories.IStatusTypeRepository;
import ru.mirea.smartdormitory.model.types.StatusType;

@ExtendWith(MockitoExtension.class)
public class StatusTypeTests {

    @Mock
    private IStatusTypeRepository statusTypeRepository;
    private StatusTypeService statusTypeService;

    @BeforeEach
    public void setUp() {
        statusTypeService = new StatusTypeService(statusTypeRepository);
    }

    @Test
    void createStatusType(){
        StatusType statusType = new StatusType(123L, "test");;

        statusTypeService.create(statusType);

        ArgumentCaptor<StatusType> statusTypeArgumentCaptor = ArgumentCaptor.forClass(StatusType.class);
        Mockito.verify(statusTypeRepository).save(statusTypeArgumentCaptor.capture());

        StatusType statusTypeCaptorValue = statusTypeArgumentCaptor.getValue();
        Assertions.assertThat(statusTypeCaptorValue).isEqualTo(statusType);
    }
}