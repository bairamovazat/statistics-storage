package ru.ivmiit.web.utils;

import org.springframework.data.mongodb.core.query.Criteria;
import ru.azat.vaadin.crud.api.Query;

public class CriteriaUtils {

    private CriteriaUtils() {

    }

    public static org.springframework.data.mongodb.core.query.Query getMongoDbQuery(Query<Criteria> query) {
        org.springframework.data.mongodb.core.query.Query mongoDbQuery
                = new org.springframework.data.mongodb.core.query.Query();

        if(query.getFilters().size() == 0) {
            return mongoDbQuery;
        }
        Criteria mainCriteria = new Criteria();
        mainCriteria.andOperator(query.getFilters().toArray(new Criteria[0]));
        mongoDbQuery.addCriteria(mainCriteria);
        return mongoDbQuery;
    }
}
