package ru.ivmiit.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.ivmiit.web.model.User;
import ru.ivmiit.web.repository.UserRepository;
import ru.ivmiit.web.security.details.Role;
import ru.ivmiit.web.utils.PasswordGenerator;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordGenerator passwordGenerator;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private EmailService emailService;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
