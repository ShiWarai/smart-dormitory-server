package ru.mirea.smartdormitory.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.smartdormitory.model.entities.Room;
import ru.mirea.smartdormitory.model.repositories.IRoomRepository;

@Service
@Transactional
public class RoomService extends AbstractService<Room, IRoomRepository> {
    @Autowired
    protected RoomService(IRoomRepository repository) {
        super(repository);
    }

    @Override
    public Room update(Long id, Room entity) {
        findById(id);
        entity.setNumber(id);
        return create(entity);
    }
}
