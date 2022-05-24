package ru.mirea.smartdormitory.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.smartdormitory.model.repositories.IStatusTypeRepository;
import ru.mirea.smartdormitory.model.types.StatusType;


@Service
@Transactional
public class StatusTypeService extends AbstractService<StatusType, IStatusTypeRepository> {

    private final IStatusTypeRepository statusTypeRepository;

    @Autowired
    protected StatusTypeService(IStatusTypeRepository repository) {
        super(repository);
        this.statusTypeRepository = repository;
    }

    public StatusType findByName(String name){
        return this.statusTypeRepository.findByName(name);
    }
}
