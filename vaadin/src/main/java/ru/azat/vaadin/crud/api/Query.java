package ru.azat.vaadin.crud.api;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * @param <F> - Тип фильтра
 */
public class Query<F> {

    private List<F> filters = new ArrayList<>();

    public Query() {
    }

    public void addFilter(List<F> filters) {
        filters.addAll(filters);
    };

    public void addFilter(F filter) {
        filters.add(filter);
    };

    public void clearFilters() {
        filters.clear();
    };

    public void removeFilter(F filter) {
        filters.remove(filter);
    };

    public List<F> getFilters() {
        return filters;
    };
}
