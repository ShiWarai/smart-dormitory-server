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
import ru.mirea.smartdormitory.model.entities.Room;
import ru.mirea.smartdormitory.repositories.IRoomRepository;
import ru.mirea.smartdormitory.model.types.RoomType;

@ExtendWith(MockitoExtension.class)
public class RoomTests {

    @Mock
    private IRoomRepository roomRepository;
    private RoomService roomService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void setUp() {
        roomService = new RoomService(roomRepository);
    }

    @Test
    void createRoom(){
        Room room = new Room(123L, 1L, new RoomType(), 1L);;

        roomService.create(room);

        ArgumentCaptor<Room> roomArgumentCaptor = ArgumentCaptor.forClass(Room.class);
        Mockito.verify(roomRepository).save(roomArgumentCaptor.capture());

        Room roomCaptorValue = roomArgumentCaptor.getValue();
        Assertions.assertThat(roomCaptorValue).isEqualTo(room);
    }
}