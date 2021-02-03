package ru.ivmiit.web.repository;

import org.springframework.data.mongodb.core.query.Criteria;
import ru.azat.vaadin.crud.api.Query;

import java.util.ArrayList;
import java.util.List;

public class MongodbQuery implements Query<Criteria> {

    private List<Criteria> criteriaList = new ArrayList<>();

    @Override
    public void addFilter(List<Criteria> list) {
        criteriaList.addAll(list);
    }

    @Override
    public void addFilter(Criteria criteria) {
        criteriaList.add(criteria);
    }

    @Override
    public void clearFilters() {
        criteriaList.clear();
    }

    @Override
    public void removeFilter(Criteria criteria) {
        criteriaList.remove(criteria);
    }

    @Override
    public List<Criteria> getFilters() {
        return criteriaList;
    }
}
