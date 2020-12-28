package ru.ivmiit.web.service;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import ru.azat.vaadin.crud.api.FilteringAndSortingCrudDao;
import ru.azat.vaadin.crud.api.Query;
import ru.ivmiit.web.model.Model;
import ru.ivmiit.web.model.User;

import java.util.List;


public interface ModelService extends FilteringAndSortingCrudDao<Model, Criteria, Query<Criteria>> {

    List<Model> getAllByCurrentUser();

    List<Model> getAllByAuthor(User user);
}
