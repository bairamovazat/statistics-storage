package ru.ivmiit.web.service;

import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import ru.azat.vaadin.crud.api.Query;
import ru.ivmiit.web.model.Data;
import ru.ivmiit.web.model.Model;
import ru.ivmiit.web.repository.DataRepository;
import ru.ivmiit.web.repository.ModelRepository;
import ru.ivmiit.web.utils.CriteriaUtils;
import ru.ivmiit.web.utils.DataUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ModelServiceImpl implements ModelService {

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Model createNew() {
        return Model.builder().modelColumnList(new HashMap<>()).build();
    }

    @Override
    public Model create(Model element) {
        return update(element);
    }

    @Override
    public Model update(Model element) {
        return modelRepository.save(element);
    }

    private void fillRandomData(Model element, int count) {
        List<Data> dataToSave = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            dataToSave.add(DataUtil.generateData(element));
        }
        dataRepository.saveAll(dataToSave);
    }


    @Override
    public void delete(Model element) {
        mongoTemplate.remove(element.getModelColumnList());
        modelRepository.delete(element);
    }

    @Override
    public Stream<Model> readAll() {
        return modelRepository.findAll().stream();
    }


    @Override
    public List<Model> getAll() {
        return modelRepository.findAll();
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
        mongoDbQuery.limit(limit);
        return mongoTemplate.find(mongoDbQuery, Model.class).stream();
    }

    @Override
    public Stream<Model> load(int offset, int limit, Optional<Query<Criteria>> query, List<QuerySortOrder> querySortOrders) {
        if (!query.isPresent()) {
            return load(offset, limit);
        }
        org.springframework.data.mongodb.core.query.Query mongoDbQuery = CriteriaUtils.getMongoDbQuery(query.get());
        mongoDbQuery.skip(offset);
        mongoDbQuery.limit(offset);

        querySortOrders.forEach(sort -> mongoDbQuery.with(Sort.by(
                sort.getDirection() == SortDirection.ASCENDING ? Sort.Direction.ASC : Sort.Direction.DESC,
                sort.getSorted()
        )));
        return mongoTemplate.find(mongoDbQuery, Model.class).stream();
    }

}
