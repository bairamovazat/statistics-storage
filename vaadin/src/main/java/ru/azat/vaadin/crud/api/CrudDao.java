package ru.azat.vaadin.crud.api;

import java.util.List;
import java.util.stream.Stream;

/**
 * Crud операции с возможностью подгрузки данных
 * @param <T> - тип данных
 */
public interface CrudDao<T> {

    List<T> load(int offset, int limit);
    /**
     * @return создаёт новый пустой элемент, который ещё не сохранён в базе даннх
     */
    T createNew();

    T create(T element);

    T update(T element);

    void delete(T element);

    List<T> readAll();

    int count();
}
