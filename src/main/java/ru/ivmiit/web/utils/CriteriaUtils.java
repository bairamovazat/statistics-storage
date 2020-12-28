package ru.ivmiit.web.utils;

import org.springframework.data.mongodb.core.query.Criteria;
import ru.azat.vaadin.crud.api.Query;

import java.util.List;

public class CriteriaUtils {

    private CriteriaUtils() {

    }

    public static org.springframework.data.mongodb.core.query.Query getMongoDbQuery(Query<Criteria> query) {
        org.springframework.data.mongodb.core.query.Query mongoDbQuery
                = new org.springframework.data.mongodb.core.query.Query();

        if(query.getFilters().size() == 0) {
            return mongoDbQuery;
        }
        mongoDbQuery.addCriteria(unionCriteria(query.getFilters()));
        return mongoDbQuery;
    }

    public static Criteria unionCriteria(List<Criteria> criteriaList) {
        Criteria mainCriteria = new Criteria();
        mainCriteria.andOperator(criteriaList.toArray(new Criteria[0]));
        return mainCriteria;
    }
}
