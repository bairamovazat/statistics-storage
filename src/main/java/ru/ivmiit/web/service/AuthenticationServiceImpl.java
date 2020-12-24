package ru.ivmiit.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import ru.ivmiit.web.model.User;
import ru.ivmiit.web.repository.UserRepository;
import ru.ivmiit.web.security.details.UserDetailsImpl;
import ru.ivmiit.web.transfer.UserDto;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return getUserByAuthentication(authentication);
    }

    @Override
    public User getUserByAuthentication(Authentication authentication) {
        if(authentication == null){
            return null;
        }
        UserDetailsImpl currentUserDetails = (UserDetailsImpl)authentication.getPrincipal();
        User currentUserModel = currentUserDetails.getUser();
        String currentUserId = currentUserModel.getId();
        return userRepository.findById(currentUserId).orElse(null);
    }

    @Override
    public void putUserToModelIfExists(Authentication authentication, ModelMap model) {
        if(authentication == null){
            model.addAttribute("user", Optional.empty());
            return;
        }
        model.addAttribute("user", Optional.of(UserDto.from(getUserByAuthentication(authentication))));
    }
}
