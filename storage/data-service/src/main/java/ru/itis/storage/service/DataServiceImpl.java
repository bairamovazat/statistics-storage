package ru.itis.storage.service;

import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.azat.vaadin.crud.LoadMultipleObject;
import ru.azat.vaadin.crud.api.Query;
import ru.itis.storage.api.DataService;
import ru.itis.storage.api.model.Data;
import ru.itis.storage.api.utils.CriteriaUtils;
import ru.itis.storage.repository.DataRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/data")
public class DataServiceImpl implements DataService {

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @GetMapping("/createNew")
    public Data createNew() {
        return Data.builder().columns(new HashMap<>()).build();
    }

    @Override
    @PostMapping("/create")
    public Data create(@RequestBody Data element) {
        return update(element);
    }

    @Override
    @PutMapping("/update")
    public Data update(@RequestBody Data element) {
        return dataRepository.save(element);
    }

    @Override
    @DeleteMapping("/delete")
    public void delete(@RequestBody Data element) {
        dataRepository.delete(element);
    }

    @Override
    @GetMapping("/readAll")
    public List<Data> readAll() {
        return dataRepository.findAll();
    }

    @Override
    @GetMapping("/count")
    public int count() {
        return (int) dataRepository.count();
    }

    @Override
    @PostMapping("/count/criteria")
    public int count(@RequestBody Optional<Query<Criteria>> query) {
        return query.map(q -> (int) mongoTemplate.count(CriteriaUtils.getMongoDbQuery(q), Data.class)).orElseGet(this::count);
    }

    @Override
    @GetMapping("/load")
    public List<Data> load(@RequestParam("offset") int offset, @RequestParam("limit") int limit){
        org.springframework.data.mongodb.core.query.Query mongoDbQuery = new org.springframework.data.mongodb.core.query.Query();
        mongoDbQuery.skip(offset);
        mongoDbQuery.limit(offset);
        return mongoTemplate.find(mongoDbQuery, Data.class);
    }

    @Override
    @PostMapping("/load/criteria")
    public List<Data> load(@RequestParam("offset") int offset, @RequestParam("limit") int limit,
                             @RequestBody LoadMultipleObject<Criteria> loadMultipleObject) {
        Optional<Query<Criteria>> query = loadMultipleObject.getQuery();
        List<QuerySortOrder> querySortOrders = loadMultipleObject.getQuerySortOrders();

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

        return mongoTemplate.find(mongoDbQuery, Data.class);
    }

}
