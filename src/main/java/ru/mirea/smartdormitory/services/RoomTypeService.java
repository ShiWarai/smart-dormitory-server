package ru.mirea.smartdormitory.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.smartdormitory.model.repositories.IRoomTypeRepository;
import ru.mirea.smartdormitory.model.types.RoomType;


@Service
@Transactional
public class RoomTypeService extends AbstractService<RoomType, IRoomTypeRepository> {

    @Autowired
    protected RoomTypeService(IRoomTypeRepository repository) {
        super(repository);
    }

    @Override
    public RoomType update(Long id, RoomType entity) {
        findById(id);
        entity.setId(id);
        return create(entity);
    }
}
