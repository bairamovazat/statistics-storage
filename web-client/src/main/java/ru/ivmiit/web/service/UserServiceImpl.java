package ru.ivmiit.web.service;

import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import ru.azat.vaadin.crud.api.Query;
import ru.ivmiit.web.model.Model;
import ru.ivmiit.web.model.User;
import ru.ivmiit.web.repository.UserRepository;
import ru.ivmiit.web.security.details.Role;
import ru.ivmiit.web.security.details.State;
import ru.ivmiit.web.utils.CriteriaUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {


    private final MongoTemplate mongoTemplate;

    private final UserRepository userRepository;

    public UserServiceImpl(@Autowired UserRepository userRepository, @Autowired MongoTemplate mongoTemplate) {
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
        insertRootUser();
    }

    public void insertRootUser() {
        org.springframework.data.mongodb.core.query.Query mongoDbQuery = new org.springframework.data.mongodb.core.query.Query();
        mongoDbQuery.addCriteria(Criteria.where("login").is("admin"));
        User user = mongoTemplate.findOne(mongoDbQuery, User.class);
        if (user == null) {
            userRepository.save(User.builder()
                    .login("admin")
                    .email("bairamovazat@gmail.com")
                    .hashPassword("$2a$10$S4k5soTqrxmUBSMixEp/aOqesQFN2ceyyxFRrFnsT0v.28hgndegq")
                    .roles(Arrays.asList(Role.USER, Role.ADMIN, Role.CREATOR))
                    .build()
            );
        } else if(!user.getRoles().contains(Role.ADMIN)) {
            user.getRoles().add(Role.ADMIN);
            userRepository.save(user);
        }
    }

    @Override
    public Stream<User> load(int offset, int limit, Optional<Query<Criteria>> query, List<QuerySortOrder> querySortOrders) {
        if (!query.isPresent()) {
            return load(offset, limit);
        }
        org.springframework.data.mongodb.core.query.Query mongoDbQuery = CriteriaUtils.getMongoDbQuery(query.get());
        mongoDbQuery.skip(offset);
        mongoDbQuery.limit(offset);

        querySortOrders.forEach(sort -> mongoDbQuery.with(Sort.by(
                sort.getDirection() == SortDirection.ASCENDING ? Sort.Direction.ASC : Sort.Direction.DESC,
                sort.getSorted()
        )));
        return mongoTemplate.find(mongoDbQuery, User.class).stream();
    }

    @Override
    public int count(Optional<Query<Criteria>> query) {
        return query.map(q -> (int) mongoTemplate.count(CriteriaUtils.getMongoDbQuery(q), User.class)).orElseGet(this::count);
    }

    @Override
    public Stream<User> load(int offset, int limit) {
        org.springframework.data.mongodb.core.query.Query mongoDbQuery = new org.springframework.data.mongodb.core.query.Query();
        mongoDbQuery.skip(offset);
        mongoDbQuery.limit(limit);
        return mongoTemplate.find(mongoDbQuery, User.class).stream();
    }

    @Override
    public User createNew() {
        return User.builder().roles(Arrays.asList(Role.USER)).build();
    }

    @Override
    public User create(User user) {
        return update(user);
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public Stream<User> readAll() {
        return userRepository.findAll().stream();
    }

    @Override
    public int count() {
        return (int) userRepository.count();
    }
}
