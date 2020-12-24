package ru.ivmiit.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import ru.azat.vaadin.crud.api.Query;
import ru.ivmiit.web.model.Model;
import ru.ivmiit.web.model.User;
import ru.ivmiit.web.repository.ModelRepository;
import ru.ivmiit.web.utils.CriteriaUtils;
import ru.ivmiit.web.utils.OffsetBasedPageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ModelServiceImpl implements ModelService {

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Model createNew() {
        return Model.builder().modelColumnList(new ArrayList<>()).build();
    }

    @Override
    public Model create(Model element) {
        return update(element);
    }

    @Override
    public Model update(Model element) {
        element.setAuthor(authenticationService.getCurrentUser());
        return modelRepository.save(element);
    }

    @Override
    public void delete(Model element) {
        modelRepository.delete(element);
    }

    @Override
    public Stream<Model> readAll() {
        return modelRepository.findAll().stream();
    }

    @Override
    public List<Model> getAllByCurrentUser() {
        return modelRepository.getAllByAuthor(authenticationService.getCurrentUser());
    }
    @Override
    public List<Model> getAllByAuthor(User user) {
        return modelRepository.getAllByAuthor(user);
    }

    @Override
    public int count() {
        return (int) modelRepository.count();
    }

    @Override
    public int count(Optional<ru.azat.vaadin.crud.api.Query<Criteria>> query) {
        return query.map(q -> (int) mongoTemplate.count(CriteriaUtils.getMongoDbQuery(q), Model.class)).orElseGet(this::count);
    }

    @Override
    public Stream<Model> load(int offset, int limit) {
        org.springframework.data.mongodb.core.query.Query mongoDbQuery = new org.springframework.data.mongodb.core.query.Query();
        mongoDbQuery.skip(offset);
        mongoDbQuery.limit(offset);
        return mongoTemplate.find(mongoDbQuery, Model.class).stream();
    }

    @Override
    public Stream<Model> load(int offset, int limit, Optional<Query<Criteria>> query) {
        if (!query.isPresent()) {
            return load(offset, limit);
        }
        org.springframework.data.mongodb.core.query.Query mongoDbQuery = CriteriaUtils.getMongoDbQuery(query.get());
        mongoDbQuery.skip(offset);
        mongoDbQuery.limit(offset);
        return mongoTemplate.find(mongoDbQuery, Model.class).stream();
    }


}
