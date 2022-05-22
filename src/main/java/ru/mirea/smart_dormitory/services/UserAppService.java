package ru.mirea.smart_dormitory.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mirea.smart_dormitory.entities.UserApp;
import ru.mirea.smart_dormitory.repositories.UserRepository;
import ru.mirea.smart_dormitory.tables.User;

@Service
public class UserAppService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserAppService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserApp applicationUser = new UserApp(userRepository.findUserByUsername(username));
        if (applicationUser.getUser() == null) {
            throw new UsernameNotFoundException(String.format("Username: %s not found", username));
        }
        return applicationUser;
    }

    public String signUpUser(User user) {
        boolean userExists = userRepository.findUserByUsername(user.getUsername()) != null;
        if (userExists) {
            throw new IllegalStateException(String.format("User with username %s exists", user.getUsername()));
        }

        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);
        return "login";
    }
}
