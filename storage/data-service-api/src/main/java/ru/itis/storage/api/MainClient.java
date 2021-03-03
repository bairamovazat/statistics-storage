package ru.itis.storage.api;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;
import ru.azat.vaadin.crud.LoadMultipleObject;
import ru.azat.vaadin.crud.api.Query;
import ru.itis.storage.api.model.Data;
import ru.itis.storage.api.model.Model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface MainClient {
    @GetMapping("/data/createNew")
    public Data createNew();

    @PostMapping("/data/create")
    public Data create(@RequestBody Data element);

    @PutMapping("/data/update")
    public Data update(@RequestBody Data element);

    @DeleteMapping("/data/delete")
    public void delete(@RequestBody Data element);

    @GetMapping("/data/readAll")
    public List<Data> readAll();

    @GetMapping("/data/count")
    public int count();

    @PostMapping("/data/count/criteria")
    public int count(@RequestBody Optional<Query<Criteria>> query);

    @GetMapping("/data/load")
    public List<Data> load(@RequestParam("offset") int offset, @RequestParam("limit") int limit);

    @PostMapping("/data/load/criteria")
    public List<Data> load(@RequestParam("offset") int offset, @RequestParam("limit") int limit,
                             @RequestBody LoadMultipleObject<Criteria> loadMultipleObject);

    @GetMapping("/model/getAll")
    public List<Model> getAllModel();

    @GetMapping("/model/createNew")
    public Model createNewModel();

    @PostMapping("/model/create")
    public Model createModel(@RequestBody Model element);

    @PutMapping("/model/update")
    public Model updateModel(@RequestBody Model element);

    @DeleteMapping("/model/delete")
    public void deleteModel(@RequestBody Model element);

    @GetMapping("/model/readAll")
    public List<Model> readAllModel();

    @GetMapping("/model/count")
    public int countModel();

    @PostMapping("/model/count/criteria")
    public int countModel(@RequestBody Optional<Query<Criteria>> query);

    @GetMapping("/model/load")
    public List<Model> loadModel(@RequestParam("offset") int offset, @RequestParam("limit") int limit);

    @PostMapping("/model/load/criteria")
    public List<Model> loadModel(@RequestParam("offset") int offset, @RequestParam("limit") int limit,
                                   @RequestBody LoadMultipleObject<Criteria> loadMultipleObject);
}
