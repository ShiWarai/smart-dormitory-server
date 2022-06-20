package ru.mirea.smartdormitory.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.smartdormitory.model.entities.Object;
import ru.mirea.smartdormitory.repositories.IObjectRepository;

@Service
@Transactional
public class ObjectService extends AbstractService<Object, IObjectRepository> {
    @Autowired
    protected ObjectService(IObjectRepository repository) {
        super(repository);
    }

    @Override
    public Object update(Long id, Object entity) {
        findById(id);
        entity.setId(id);
        return create(entity);
    }
}
