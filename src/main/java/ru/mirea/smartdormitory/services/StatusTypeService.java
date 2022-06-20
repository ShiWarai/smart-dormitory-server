package ru.mirea.smartdormitory.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.smartdormitory.repositories.IStatusTypeRepository;
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

    @Bean
    public void createBaseStatuses(){
        StatusType ready = new StatusType(100L, "ready");
        if (statusTypeRepository.findById(ready.getId()).isEmpty())
            statusTypeRepository.save(ready);

        StatusType busy = new StatusType(200L, "busy");
        if (statusTypeRepository.findById(busy.getId()).isEmpty())
            statusTypeRepository.save(busy);
    }
}
