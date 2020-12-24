package ru.ivmiit.web.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.ivmiit.web.model.Data;
import ru.ivmiit.web.model.Model;

import java.util.List;

@Repository
public interface DataRepository extends MongoRepository<Data, String> {
    List<Data> findAllByModel(Model model);
}
