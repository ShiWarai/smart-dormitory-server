package ru.mirea.smartdormitory.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.smartdormitory.model.entities.Resident;
import ru.mirea.smartdormitory.repositories.IResidentRepository;
import ru.mirea.smartdormitory.model.types.RoleType;

@Service
@Transactional
public class ResidentService extends AbstractService<Resident, IResidentRepository> implements UserDetailsService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final IResidentRepository residentRepository;

    @Autowired
    protected ResidentService(IResidentRepository repository) {
        super(repository);
        this.residentRepository = repository;
    }

    @Override
    public Resident create(Resident entity) {
        entity.setPinCode(bCryptPasswordEncoder.encode(entity.getPinCode()));
        return residentRepository.save(entity);
    }

    @Override
    public Resident update(Long id, Resident entity) {
        if(getById(id) != null) {
            entity.setPinCode(bCryptPasswordEncoder.encode(entity.getPinCode()));
            residentRepository.updatePropertiesByStudentId(entity.getStudentId(),
                                                            entity.getSurname(),
                                                            entity.getName(),
                                                            entity.getPatronymic(),
                                                            entity.getBirthdate(),
                                                            entity.getPinCode(),
                                                            entity.getRoomNumber(),
                                                            entity.getRole());
            return getById(id);
        }
        else
            return null;
    }

    public RoleType getRoleTypeByStudentId(String student_id) {
        return RoleType.valueOf(getByStudentId(student_id).getRole());
    }

    public Resident getByStudentId(String username){
        return residentRepository.findResidentByStudentId(username);
    }

    @Bean
    public void createBaseResidents(){
        Resident resident = new Resident();
        resident.setSurname("The");
        resident.setName("Admin");
        resident.setStudentId("1234567");
        resident.setPinCode("1111");
        resident.setRole(RoleType.COMMANDANT.name());
        if (residentRepository.findResidentByStudentId(resident.getStudentId())==null) {
            create(resident);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String student_id) throws UsernameNotFoundException {
        UserDetails user =  residentRepository.findResidentByStudentId(student_id);
        if(user != null){
            return user;
        }
        throw new
                UsernameNotFoundException("Resident not exist with student ID:" + student_id);
    }
}
