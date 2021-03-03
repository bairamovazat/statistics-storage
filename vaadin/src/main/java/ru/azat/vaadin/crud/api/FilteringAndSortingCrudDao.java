package ru.azat.vaadin.crud.api;

import com.vaadin.flow.data.provider.QuerySortOrder;
import ru.azat.vaadin.crud.LoadMultipleObject;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Crud операции с возможностью подгрузки данных
 * @param <T> - тип данных
 * @param <F> - Фильтр
 * @param <Q> - объект для фильтрации и тд
 */
public interface FilteringAndSortingCrudDao<T, F, Q extends Query<F>> extends CrudDao<T>{

    List<T> load(int offset, int limit, LoadMultipleObject<F> loadMultipleObject);

    int count(Optional<Q> filter);

}
