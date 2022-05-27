package ru.mirea.smartdormitory.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.smartdormitory.model.entities.Resident;
import ru.mirea.smartdormitory.model.repositories.IResidentRepository;
import ru.mirea.smartdormitory.model.types.RoleType;

import java.sql.Date;

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

    public RoleType getByStudentId(String student_id) {
        return RoleType.valueOf(findByStudentId(student_id).getRole());
    }

    public Resident findByStudentId(String username){
        return residentRepository.findResidentByStudentId(username);
    }

    public void create(String student_id, String pin_code, String surname, String name, String patronymic, Date birthday, String role) {
        Resident resident = new Resident();

        resident.setSurname(surname);
        resident.setName(name);
        if(patronymic.length() > 0)
            resident.setPatronymic(patronymic);
        resident.setBirthdate(birthday);
        resident.setStudentId(student_id);
        resident.setPinCode(bCryptPasswordEncoder.encode(pin_code));
        resident.setRole(role);

        residentRepository.save(resident);
        System.out.println(residentRepository.findResidentByStudentId(student_id));
    }

    @Bean
    public void createDef(){
        Resident resident = new Resident();
        resident.setSurname("The");
        resident.setName("Admin");
        resident.setStudentId("1234567");
        resident.setPinCode(bCryptPasswordEncoder.encode("1111"));
        resident.setRole(RoleType.COMMANDANT.name());
        if (residentRepository.findResidentByStudentId(resident.getStudentId())==null) {
            residentRepository.save(resident);
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
