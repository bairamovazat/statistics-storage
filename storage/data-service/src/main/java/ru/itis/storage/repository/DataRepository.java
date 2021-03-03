package ru.itis.storage.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.itis.storage.api.model.Data;
import ru.itis.storage.api.model.Model;

import java.util.List;

@Repository
public interface DataRepository extends MongoRepository<Data, String> {
    List<Data> findAllByModel(Model model);
}
