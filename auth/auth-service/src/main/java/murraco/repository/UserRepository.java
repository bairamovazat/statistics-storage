package murraco.repository;


import org.azat.oauth.model.User;

public interface UserRepository {

  boolean existsByUsername(String username);

  User findByUsername(String username);

  void deleteByUsername(String username);

  void save(User user);
}
