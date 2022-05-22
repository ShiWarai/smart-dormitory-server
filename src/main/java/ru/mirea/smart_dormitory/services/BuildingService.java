package ru.mirea.smart_dormitory.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.smart_dormitory.repositories.BuildingRepository;
import ru.mirea.smart_dormitory.tables.Building;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BuildingService implements TableService<Building>{

    private BuildingRepository buildingRepository;

    @Autowired
    BuildingService(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }

    public void createEntity(Building building) {
        buildingRepository.save(building);
    }


    public List<Building> readAllEntity() {
        return buildingRepository.findAll();
    }

    public boolean deleteAllEntity() {
        buildingRepository.deleteAll();
        return true;
    }

    public List<Building> filterBy(String date, String type) {
        if(date != null && type != null)
            return buildingRepository.findAllByDateAndType(date, type);
        else if (date != null) {
            return buildingRepository.findAllByDate(date);
        } else if (type != null) {
            return buildingRepository.findAllByType(type);
        }

        return null;
    }
}
