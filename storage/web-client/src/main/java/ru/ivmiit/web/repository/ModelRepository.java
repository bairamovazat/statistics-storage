package ru.ivmiit.web.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.ivmiit.web.model.Model;
import ru.ivmiit.web.model.User;

import java.util.List;

@Repository
public interface ModelRepository extends MongoRepository<Model, String> {

    List<Model> getAllByAuthor(User user);
}
