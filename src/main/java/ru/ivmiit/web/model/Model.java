package ru.ivmiit.web.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

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

    @DBRef
    private User author;

    private List<ModelColumn> modelColumnList = new ArrayList<>();

}
