package ru.itis.storage.api.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public static ModelColumn duplicate(ModelColumn original) {
        return ModelColumn.builder()
                .uniqueColumnId(original.getUniqueColumnId())
                .columnName(original.getColumnName())
                .columnType(original.getColumnType())
                .build();
    }

    public static List<ModelColumn> duplicate(List<ModelColumn> originalList) {
        return originalList.stream().map(ModelColumn::duplicate).collect(Collectors.toList());
    }

}
