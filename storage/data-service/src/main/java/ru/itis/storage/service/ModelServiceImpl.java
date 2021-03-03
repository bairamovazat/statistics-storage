package ru.itis.storage.service;

import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;
import ru.azat.vaadin.crud.LoadMultipleObject;
import ru.azat.vaadin.crud.api.Query;
import ru.itis.storage.api.ModelService;
import ru.itis.storage.api.model.Data;
import ru.itis.storage.api.model.Model;
import ru.itis.storage.repository.DataRepository;
import ru.itis.storage.repository.ModelRepository;
import ru.itis.storage.api.utils.CriteriaUtils;
import ru.itis.storage.api.utils.DataUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/model")
public class ModelServiceImpl implements ModelService {

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @GetMapping("/createNew")
    public Model createNew(){
        return Model.builder().modelColumnList(new HashMap<>()).build();
    }

    @Override
    @PostMapping("/create")
    public Model create(@RequestBody Model element) {
        return update(element);
    }

    @Override
    @PutMapping("/update")
    public Model update(@RequestBody Model element) {
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
    @DeleteMapping("/delete")
    public void delete(@RequestBody Model element) {
        mongoTemplate.remove(element.getModelColumnList());
        modelRepository.delete(element);
    }

    @Override
    @GetMapping("/readAll")
    public List<Model> readAll() {
        return modelRepository.findAll();
    }


    @GetMapping("/getAll")
    public List<Model> getAll() {
        return modelRepository.findAll();
    }

    @Override
    @GetMapping("/count")
    public int count() {
        return (int) modelRepository.count();
    }

    @Override
    @PostMapping("/count/criteria")
    public int count(@RequestBody Optional<ru.azat.vaadin.crud.api.Query<Criteria>> query) {
        return query.map(q -> (int) mongoTemplate.count(CriteriaUtils.getMongoDbQuery(q), Model.class)).orElseGet(this::count);
    }

    @Override
    @GetMapping("/load")
    public List<Model> load(@RequestParam("offset") int offset, @RequestParam("limit") int limit) {
        org.springframework.data.mongodb.core.query.Query mongoDbQuery = new org.springframework.data.mongodb.core.query.Query();
        mongoDbQuery.skip(offset);
        mongoDbQuery.limit(limit);
        return mongoTemplate.find(mongoDbQuery, Model.class);
    }


    @Override
    @PostMapping("/load/criteria")
    public List<Model> load(@RequestParam("offset") int offset, @RequestParam("limit") int limit,
                              @RequestBody LoadMultipleObject<Criteria> loadMultipleObject) {
        Optional<Query<Criteria>> query = loadMultipleObject.getQuery();
        List<QuerySortOrder> querySortOrders = loadMultipleObject.getQuerySortOrders();
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
        return mongoTemplate.find(mongoDbQuery, Model.class);
    }

}
