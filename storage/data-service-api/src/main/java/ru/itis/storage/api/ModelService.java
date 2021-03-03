package ru.itis.storage.api;

import com.vaadin.flow.data.provider.QuerySortOrder;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;
import ru.azat.vaadin.crud.LoadMultipleObject;
import ru.azat.vaadin.crud.api.FilteringAndSortingCrudDao;
import ru.azat.vaadin.crud.api.Query;
import ru.itis.storage.api.model.Data;
import ru.itis.storage.api.model.Model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


@RequestMapping("/model")
public interface ModelService extends FilteringAndSortingCrudDao<Model, Criteria, Query<Criteria>> {

    @GetMapping("/getAll")
    public List<Model> getAll();

    @Override
    @GetMapping("/createNew")
    public Model createNew();

    @Override
    @PostMapping("/create")
    public Model create(@RequestBody Model element);

    @Override
    @PutMapping("/update")
    public Model update(@RequestBody Model element);

    @Override
    @DeleteMapping("/delete")
    public void delete(@RequestBody Model element);

    @Override
    @GetMapping("/readAll")
    public List<Model> readAll();

    @Override
    @GetMapping("/count")
    public int count();

    @Override
    @PostMapping("/count/criteria")
    public int count(@RequestBody Optional<Query<Criteria>> query);

    @Override
    @GetMapping("/load")
    public List<Model> load(@RequestParam("offset") int offset, @RequestParam("limit") int limit);

    @Override
    @PostMapping("/load/criteria")
    public List<Model> load(@RequestParam("offset") int offset, @RequestParam("limit") int limit,
                              @RequestBody LoadMultipleObject<Criteria> loadMultipleObject);

}
