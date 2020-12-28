package ru.ivmiit.web.service;

import org.springframework.data.mongodb.core.query.Criteria;

import ru.azat.vaadin.crud.api.FilteringAndSortingCrudDao;
import ru.azat.vaadin.crud.api.Query;
import ru.ivmiit.web.model.Data;


public interface DataService extends FilteringAndSortingCrudDao<Data, Criteria, Query<Criteria>> {

}
