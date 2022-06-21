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


    @Autowired
    protected StatusTypeService(IStatusTypeRepository repository) {
        super(repository);
    }

    @Bean
    public void createBaseStatuses(){
        StatusType ready = new StatusType(100L, "ready");
        if (repository.findById(ready.getId()).isEmpty())
            repository.save(ready);

        StatusType busy = new StatusType(200L, "busy");
        if (repository.findById(busy.getId()).isEmpty())
            repository.save(busy);
    }
}
