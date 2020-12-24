package ru.ivmiit.web.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ModelColumn {

    @Builder.Default
    private String uniqueColumnId = UUID.randomUUID().toString();

    private String columnName;

    private ModelColumnType columnType;

}
