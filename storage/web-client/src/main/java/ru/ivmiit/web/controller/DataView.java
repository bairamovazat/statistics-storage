package ru.ivmiit.web.controller;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.access.annotation.Secured;
import ru.azat.vaadin.crud.LoadMultipleObject;
import ru.azat.vaadin.crud.api.Query;
import ru.azat.vaadin.crud.common.BasicView;
import ru.azat.vaadin.crud.common.ColumnDefinition;
import ru.azat.vaadin.crud.common.CrudGrid;
import ru.itis.storage.api.DataService;
import ru.itis.storage.api.MongodbQuery;
import ru.itis.storage.api.model.*;
import ru.itis.storage.api.utils.DateUtil;
import ru.ivmiit.web.client.MainClientImpl;
import ru.ivmiit.web.utils.LinkUtils;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Route("data")
@Secured("USER")
@EnableFeignClients
public class DataView extends BasicView {
    private final Div content = new Div();


    private final MainClientImpl mainClient;

    private CrudGrid<Data, Criteria> grid;


    public DataView(@Autowired MainClientImpl mainClient, @Autowired MainClientImpl dataService) {
        this.mainClient = mainClient;

        addRouterLinkToDrawer(LinkUtils.getRouterLinks());

        initSelectModel();

        content.setWidth("100%");
        content.setHeight("100%");
        setContent(content);
    }

    private void initSelectModel() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setHeight("110px");
        Select<Model> valueSelect = new Select<>();
        List<Model> modelList = mainClient.getAllModel();
        valueSelect.setItemLabelGenerator(Model::getName);
        valueSelect.setItems(modelList);
        valueSelect.addValueChangeListener(e -> modelChanged(e.getValue()));
        verticalLayout.add(new Label("Выберите Форму"));
        verticalLayout.add(valueSelect);
        content.add(verticalLayout);
    }

    private void modelChanged(Model model) {
        if (grid != null) {
            content.remove(grid);
        }
        ArrayList<Supplier<Criteria>> arrayList = new ArrayList<>();
        arrayList.add(() -> Criteria.where("model").is(model));
        grid = new CrudGrid<>(getDataService(),
                createColumns(model),
                arrayList,
                new MongodbQuery(),
                true
        );

        grid.setHeight("calc(100% - 110px)");
        grid.setBeforeSave(data -> data.setModel(model));
        content.add(grid);
    }

    private Component bindColumn(ModelColumn column, Binder<Data> binder) {
        Component component;
        ModelColumnType type = column.getColumnType();
        if (type.equals(ModelColumnType.Date)) {
            DateTimePicker dateTimePicker = new DateTimePicker();
            dateTimePicker.setDatePlaceholder("Дата");
            dateTimePicker.setTimePlaceholder("Время");

            component = dateTimePicker;
            binder.forField(dateTimePicker)
                    .bind(e -> DateUtil.convertToLocalDateTime(getValue(e, column, Date.class)),
                            (e, v) -> setValue(e, column, DateUtil.convertToDate(v))
                    );

        } else if (type.equals(ModelColumnType.Number)) {
            NumberField nameField = new NumberField();
            component = nameField;
            binder.forField(nameField)
                    .bind(e -> getValue(e, column, Double.class), (e, v) -> setValue(e, column, v));
        } else {
            TextField nameField = new TextField();
            component = nameField;
            binder.forField(nameField)
                    .bind(e -> getValue(e, column, String.class), (e, v) -> setValue(e, column, v));
        }
        return component;
    }

    private <T> T getValue(Data data, ModelColumn column, Class<T> expectedClass) {
        if (data.getColumns() == null) {
            return null;
        }
        Column dataColumn = data.getColumns().getOrDefault(column.getUniqueColumnId(), null);
        if (dataColumn == null) {
            return null;
        }

        if (expectedClass.isInstance(dataColumn.getValue())) {
            return (T) dataColumn.getValue();
        } else {
            return null;
        }
    }

    private void setValue(Data data, ModelColumn modelColumn, Object value) {
        if (data.getColumns() == null) {
            data.setColumns(new HashMap<>());
        }
        Column column = data.getColumns().get(modelColumn.getUniqueColumnId());
        if (column == null) {
            column = Column.builder().uniqueColumnId(modelColumn.getUniqueColumnId()).build();
            data.getColumns().put(modelColumn.getUniqueColumnId(), column);
        }

        column.setValue(value);
    }

    private List<ColumnDefinition<Data, Criteria>> createColumns(Model model) {
        return model.getModelColumnList().entrySet().stream()
                .map(entry -> ColumnDefinition.<Data, Criteria>builder()
                        .columnName(entry.getValue().getColumnName())
                        .getter(e -> getValue(e, entry.getValue(), entry.getValue().getColumnType().getJavaClass()))
                        .bind((modelBinder) -> bindColumn(entry.getValue(), modelBinder))
                        .sortable(true)
                        .sortProperty("columns." + entry.getValue().getUniqueColumnId() + ".stringValue")
                        .filter(
                                (fieldValue) -> Criteria.where("columns." + entry.getValue().getUniqueColumnId() + ".stringValue").regex(fieldValue)
                        )
                        .editable(true)
                        .build()).collect(Collectors.toList());
    }

    private DataService getDataService() {
        return new DataService() {
            @Override
            public Data createNew() {
                return mainClient.createNew();
            }

            @Override
            public Data create(Data element) {
                return mainClient.create(element);
            }

            @Override
            public Data update(Data element) {
                return mainClient.update(element);
            }

            @Override
            public void delete(Data element) {
                mainClient.delete(element);
            }

            @Override
            public List<Data> readAll() {
                return mainClient.readAll();
            }

            @Override
            public int count() {
                return mainClient.count();
            }

            @Override
            public int count(Optional<Query<Criteria>> query) {
                return mainClient.count(query);
            }

            @Override
            public List<Data> load(int offset, int limit) {
                return mainClient.load(offset, limit);
            }

            @Override
            public List<Data> load(int offset, int limit, LoadMultipleObject<Criteria> loadMultipleObject) {
                return mainClient.load(offset, limit, loadMultipleObject);
            }
        };
    }

}
