package ru.mirea.smartdormitory.services;

import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.smartdormitory.model.entities.Object;
import ru.mirea.smartdormitory.model.entities.Resident;
import ru.mirea.smartdormitory.model.types.ObjectType;
import ru.mirea.smartdormitory.model.types.RoleType;
import ru.mirea.smartdormitory.repositories.IObjectRepository;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ObjectService extends AbstractService<Object, IObjectRepository> {
    @Autowired
    protected ObjectService(IObjectRepository repository) {
        super(repository);
    }

    @Override
    public Object update(Long id, Object entity) {
        getById(id);
        entity.setId(id);
        return create(entity);
    }

    public List<Object> getAllByRoomNumber(long roomNumber) {
        return repository.findAllByRoomNumber(roomNumber);
    }

    public List<Object> getAllCanBeReserved(ReservationService reservationService)
    {
        List<Object> objectsAllCanBeReserved = new ArrayList<Object>();
        for (Object object:getAll()) {
            if(canBeReserved(object, reservationService))
                objectsAllCanBeReserved.add(object);
        }

        return objectsAllCanBeReserved;
    }

    public boolean canBeReserved(Object object, ReservationService reservationService)
    {
        ObjectType objectType = object.getType();

        if(!object.isAvailable())
            return false;

        if(objectType.getReservationLimit() != null) {
            int count = reservationService.getAllIdByObject(object.getId()).size(); // Calculating is need
            if((count + 1) > object.getType().getReservationLimit())
                return false;
        }

        String cronStr = object.getType().getSchedule();
        if(!(cronStr == null || cronStr.isBlank())) {
            try {
                CronExpression expression = new CronExpression(cronStr);
                if (!expression.isSatisfiedBy(new Date()))
                    return false;
            } catch (ParseException exp) {
                System.out.printf("WRONG CRON: %s\n", cronStr);
                return false;
            }
        }

        return true;
    }
}
