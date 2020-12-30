package ru.ivmiit.web.service;

import org.springframework.data.mongodb.core.query.Criteria;
import ru.azat.vaadin.crud.api.FilteringAndSortingCrudDao;
import ru.azat.vaadin.crud.api.Query;
import ru.ivmiit.web.model.User;

public interface UserService extends FilteringAndSortingCrudDao<User, Criteria, Query<Criteria>> {
}
