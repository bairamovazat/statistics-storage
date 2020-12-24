package ru.ivmiit.web.service;

import org.springframework.data.mongodb.core.query.Criteria;

import ru.azat.vaadin.crud.api.FilteringCrudDao;
import ru.azat.vaadin.crud.api.Query;
import ru.ivmiit.web.model.Data;


public interface DataService extends FilteringCrudDao<Data, Criteria, Query<Criteria>> {

}
