package ru.itis.storage.api;

import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import org.springframework.web.bind.annotation.*;
import ru.azat.vaadin.crud.LoadMultipleObject;
import ru.azat.vaadin.crud.api.FilteringAndSortingCrudDao;
import ru.azat.vaadin.crud.api.Query;
import ru.itis.storage.api.model.Data;
import ru.itis.storage.api.utils.CriteriaUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RequestMapping("/data")
public interface DataService extends FilteringAndSortingCrudDao<Data, Criteria, Query<Criteria>> {

    @Override
    @GetMapping("/createNew")
    public Data createNew();

    @Override
    @PostMapping("/create")
    public Data create(@RequestBody Data element);

    @Override
    @PutMapping("/update")
    public Data update(@RequestBody Data element);

    @Override
    @DeleteMapping("/delete")
    public void delete(@RequestBody Data element);

    @Override
    @GetMapping("/readAll")
    public List<Data> readAll();

    @Override
    @GetMapping("/count")
    public int count();

    @Override
    @PostMapping("/count/criteria")
    public int count(@RequestBody Optional<Query<Criteria>> query);

    @Override
    @GetMapping("/load")
    public List<Data> load(@RequestParam("offset") int offset, @RequestParam("limit") int limit);

    @Override
    @PostMapping("/load/criteria")
    public List<Data> load(@RequestParam("offset") int offset, @RequestParam("limit") int limit,
                             @RequestBody LoadMultipleObject<Criteria> loadMultipleObject);
}
