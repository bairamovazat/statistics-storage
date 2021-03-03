package ru.ivmiit.web.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Document(collection = "DataModel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Model {

    @Id
    private String id;

    private String name;

    private Map<String, ModelColumn> modelColumnList;

    public List<ModelColumn> getModelColumnListAsArray() {
        return new ArrayList<>(modelColumnList.values());
    }

    public void setModelColumnListFromArray(List<ModelColumn> modelColumnList) {
        Map<String, ModelColumn> newModelColumns = modelColumnList.stream()
                .collect(Collectors.toMap(ModelColumn::getUniqueColumnId, e -> e));
        this.modelColumnList = newModelColumns;
    }

}
