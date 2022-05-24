package ru.mirea.smartdormitory.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.smartdormitory.model.repositories.IObjectTypeRepository;
import ru.mirea.smartdormitory.model.types.ObjectType;


@Service
@Transactional
public class ObjectTypeService extends AbstractService<ObjectType, IObjectTypeRepository> {

    @Autowired
    protected ObjectTypeService(IObjectTypeRepository repository) {
        super(repository);
    }

}
