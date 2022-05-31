package ru.mirea.smartdormitory.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.smartdormitory.model.entities.Room;
import ru.mirea.smartdormitory.model.repositories.IRoomRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoomService extends AbstractService<Room, IRoomRepository> {
    @Autowired
    protected RoomService(IRoomRepository repository) {
        super(repository);
    }

    @Override
    public Room update(Long number, Room entity) {
        if(findById(number) != null)
            return repository.save(entity);
        else
            return null;
    }

    @Override
    public Room findById(Long number) {
        // id = number
        return repository.findByNumber(number);
    }

    @Override
    public List<Room> getAll() {
        Sort sort = Sort.by("number").ascending();
        return repository.findAll(sort);
    }

    @Override
    public boolean delete(Long number) {
        // id = number
        if (repository.findByNumber(number) == null)
            return false;

        repository.deleteByNumber(number);
        return true;
    }
}
