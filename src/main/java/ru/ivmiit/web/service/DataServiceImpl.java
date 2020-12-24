package ru.ivmiit.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import ru.azat.vaadin.crud.api.Query;
import ru.ivmiit.web.model.Data;
import ru.ivmiit.web.model.Model;
import ru.ivmiit.web.repository.DataRepository;
import ru.ivmiit.web.utils.CriteriaUtils;
import ru.ivmiit.web.utils.OffsetBasedPageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;

@Service
public class DataServiceImpl implements DataService {

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Data createNew() {
        return Data.builder().columns(new ArrayList<>()).build();
    }

    @Override
    public Data create(Data element) {
        return update(element);
    }

    @Override
    public Data update(Data element) {
        return dataRepository.save(element);
    }

    @Override
    public void delete(Data element) {
        dataRepository.delete(element);
    }

    @Override
    public Stream<Data> readAll() {
        return dataRepository.findAll().stream();
    }

    @Override
    public int count() {
        return (int) dataRepository.count();
    }

    @Override
    public int count(Optional<Query<Criteria>> query) {
        return query.map(q -> (int) mongoTemplate.count(CriteriaUtils.getMongoDbQuery(q), Data.class)).orElseGet(this::count);
    }

    @Override
    public Stream<Data> load(int offset, int limit) {
        org.springframework.data.mongodb.core.query.Query mongoDbQuery = new org.springframework.data.mongodb.core.query.Query();
        mongoDbQuery.skip(offset);
        mongoDbQuery.limit(offset);
        return mongoTemplate.find(mongoDbQuery, Data.class).stream();
    }

    @Override
    public Stream<Data> load(int offset, int limit, Optional<Query<Criteria>> query) {
        if (!query.isPresent()) {
            return load(offset, limit);
        }
        org.springframework.data.mongodb.core.query.Query mongoDbQuery = CriteriaUtils.getMongoDbQuery(query.get());
        mongoDbQuery.skip(offset);
        mongoDbQuery.limit(offset);
        return mongoTemplate.find(mongoDbQuery, Data.class).stream();
    }

}
