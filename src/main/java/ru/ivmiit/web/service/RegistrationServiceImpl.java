package ru.ivmiit.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ivmiit.web.forms.UserRegistrationForm;
import ru.ivmiit.web.model.User;
import ru.ivmiit.web.repository.UserRepository;
import ru.ivmiit.web.security.details.Role;
import ru.ivmiit.web.security.details.State;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Value("${site.url}")
    private String siteUrl;

    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    @Override
    @Transactional
    public void register(UserRegistrationForm userForm) {
        userRepository.findOneByLogin(userForm.getLogin()).ifPresent(s -> {
            throw new IllegalArgumentException("");
        });
        User newUser = User.builder()
                .login(userForm.getLogin())
                .hashPassword(passwordEncoder.encode(userForm.getPassword()))
                .email(userForm.getEmail())
                .roles(new ArrayList<>())
                .build();
        newUser.getRoles().add(Role.USER);

        userRepository.save(newUser);
    }

    @Override
    public boolean userIsPresent(String login) {
        return getUserByLogin(login).isPresent();
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        return userRepository.findOneByLogin(login);
    }
}
