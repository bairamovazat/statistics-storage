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
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import ru.azat.vaadin.crud.common.BasicView;
import ru.azat.vaadin.crud.common.ColumnDefinition;
import ru.azat.vaadin.crud.common.CrudGrid;
import ru.ivmiit.web.model.*;
import ru.ivmiit.web.repository.MongodbQuery;
import ru.ivmiit.web.service.DataService;
import ru.ivmiit.web.service.ModelService;
import ru.ivmiit.web.utils.DateUtil;

import java.util.*;
import java.util.stream.Collectors;

@Route("model")
public class DataView extends BasicView {
    private final Div content = new Div();

    private final ModelService modelService;
    private final DataService dataService;

    private CrudGrid<Data, Criteria> grid;

    public DataView(@Autowired ModelService modelService, @Autowired DataService dataService) {
        this.modelService = modelService;
        this.dataService = dataService;

        addRouterLinkToDrawer(new RouterLink("Формы", ModelView.class));
        addRouterLinkToDrawer(new RouterLink("Данные", DataView.class));

        initSelectModel();

        content.setWidth("100%");
        content.setHeight("100%");
        setContent(content);
    }

    private void initSelectModel() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setHeight("110px");
        Select<Model> valueSelect = new Select<>();
        List<Model> modelList = modelService.getAllByCurrentUser();
        valueSelect.setItemLabelGenerator(Model::getName);
        valueSelect.setItems(modelList);
        valueSelect.addValueChangeListener(e -> modelChanged(e.getValue()));
        verticalLayout.add(new Label("Выберите Форму для изменения"));
        verticalLayout.add(valueSelect);
        content.add(verticalLayout);
    }

    private void modelChanged(Model model) {
        if (grid != null) {
            content.remove(grid);
        }
        grid = new CrudGrid<>(dataService, createColumns(model), Arrays.asList(() -> Criteria.where("model").is(model)), new MongodbQuery());
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

}
