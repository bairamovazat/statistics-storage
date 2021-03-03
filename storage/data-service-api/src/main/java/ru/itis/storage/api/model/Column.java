package ru.itis.storage.api.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Column {

    private String uniqueColumnId;

    private Object value;

    private String stringValue;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.stringValue = value.toString();
        this.value = value;
    }
}
