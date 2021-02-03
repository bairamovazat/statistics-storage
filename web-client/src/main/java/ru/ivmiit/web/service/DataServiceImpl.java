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
import ru.ivmiit.web.repository.DataRepository;
import ru.ivmiit.web.utils.CriteriaUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class DataServiceImpl implements DataService {

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Data createNew() {
        return Data.builder().columns(new HashMap<>()).build();
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
    public Stream<Data> load(int offset, int limit, Optional<Query<Criteria>> query, List<QuerySortOrder> querySortOrders) {
        if (!query.isPresent()) {
            return load(offset, limit);
        }

        org.springframework.data.mongodb.core.query.Query mongoDbQuery = new org.springframework.data.mongodb.core.query.Query();
        mongoDbQuery.addCriteria(CriteriaUtils.unionCriteria(query.get().getFilters()));

        mongoDbQuery.limit(limit);
        mongoDbQuery.skip(offset);
        querySortOrders.forEach(sort -> mongoDbQuery.with(Sort.by(
                sort.getDirection() == SortDirection.ASCENDING ? Sort.Direction.ASC : Sort.Direction.DESC,
                sort.getSorted()
        )));

        return mongoTemplate.find(mongoDbQuery, Data.class).stream();
    }

}
