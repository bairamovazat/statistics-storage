package ru.itis.storage.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.itis.storage.api.model.Model;

@Repository
public interface ModelRepository extends MongoRepository<Model, String> {

}
