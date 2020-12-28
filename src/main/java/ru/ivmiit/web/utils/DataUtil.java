package ru.ivmiit.web.utils;

import ru.ivmiit.web.model.Column;
import ru.ivmiit.web.model.Data;
import ru.ivmiit.web.model.Model;
import ru.ivmiit.web.model.ModelColumn;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class DataUtil {

    public static Data generateData(Model model) {
       return Data.builder()
                .model(model)
                .columns(generateColumns(model.getModelColumnList()))
                .build();
    }

    public static Map<String, Column> generateColumns(Map<String, ModelColumn> modelColumnList) {
        Map<String, Column> columnData = new HashMap<>();
        for (Map.Entry<String, ModelColumn> entry : modelColumnList.entrySet()) {
            ModelColumn value = entry.getValue();
            Object valueData;
            switch (value.getColumnType()) {
                case Number:
                    valueData = (double) randomInt();
                    break;
                case Date:
                    valueData = randomDate();
                    break;
                case String:
                case Base64:
                default:
                    valueData = Integer.toString(randomInt());
                    break;
            }

            columnData.put(entry.getKey(), Column.builder()
                    .value(valueData)
                    .stringValue(valueData.toString())
                    .uniqueColumnId(entry.getKey())
                    .build());
        }
        return columnData;
    }

    public static Date randomDate() {
        return new Date(ThreadLocalRandom.current().nextInt() * 1000L);
    }

    public static int randomInt() {
        return ThreadLocalRandom.current().nextInt();
    }
}
