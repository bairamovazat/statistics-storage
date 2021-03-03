package murraco.repository;

import org.azat.oauth.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserRepositoryImpl implements UserRepository {

    private List<User> userList = new ArrayList<>();

    @Override
    public boolean existsByUsername(String username) {
        return userList.stream().anyMatch(e -> Objects.equals(e.getUsername(), username));
    }

    @Override
    public User findByUsername(String username) {
        return userList.stream().filter(e -> Objects.equals(e.getUsername(), username)).findFirst().orElse(null);
    }

    @Override
    public void deleteByUsername(String username) {
        userList.stream().filter(e -> Objects.equals(e.getUsername(), username))
                .findFirst()
                .ifPresent(user -> userList.remove(user));
    }

    @Override
    public void save(User user) {
        userList.add(user);
    }
}
