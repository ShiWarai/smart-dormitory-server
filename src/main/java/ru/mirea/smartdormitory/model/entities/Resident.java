package ru.mirea.smartdormitory.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "resident")
@Setter
@Getter
@NoArgsConstructor
@JsonIgnoreProperties({"enabled",
                        "password",
                        "username",
                        "authorities",
                        "accountNonExpired",
                        "accountNonLocked",
                        "credentialsNonExpired"})
public class Resident implements UserDetails {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "surname", nullable = false, length = 50)
    private String surname;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "patronymic", length = 50)
    private String patronymic;

    @Column(name = "birthdate")
    private java.sql.Date birthdate;

    @Column(name = "student_id", unique = true, nullable = false)
    private String studentId;

    @Column(name = "pin_code", length = 256)
    private String pinCode;

    @Column(name = "room_number")
    private Long roomNumber;

    @Column(name = "role", length = 16, nullable = false)
    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return studentId;
    }

    @Override
    public String getPassword() {
        return pinCode;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
