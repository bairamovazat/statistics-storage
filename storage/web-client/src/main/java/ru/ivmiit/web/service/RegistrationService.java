package ru.ivmiit.web.service;

import ru.ivmiit.web.forms.UserRegistrationForm;
import ru.ivmiit.web.model.User;

import java.util.Optional;

public interface RegistrationService {
    void register(UserRegistrationForm userForm);

    boolean userIsPresent(String login);

    Optional<User> getUserByLogin(String login);
}
