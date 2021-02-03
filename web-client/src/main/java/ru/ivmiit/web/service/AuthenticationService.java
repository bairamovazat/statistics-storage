package ru.ivmiit.web.service;

import org.springframework.security.core.Authentication;
import org.springframework.ui.ModelMap;
import ru.ivmiit.web.model.User;

public interface AuthenticationService {
    User getCurrentUser();

    User getUserByAuthentication(Authentication authentication);
}
