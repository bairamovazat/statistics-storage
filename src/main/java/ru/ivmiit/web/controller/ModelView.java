package ru.ivmiit.web.controller;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.azat.vaadin.crud.common.BasicView;
import ru.azat.vaadin.crud.common.ColumnDefinition;
import ru.azat.vaadin.crud.common.CrudGrid;
import ru.azat.vaadin.crud.common.DialogGrid;
import ru.ivmiit.web.model.Model;
import ru.ivmiit.web.model.ModelColumn;
import ru.ivmiit.web.model.ModelColumnType;
import ru.ivmiit.web.repository.MongodbQuery;
import ru.ivmiit.web.service.ModelService;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Route("data")
public class ModelView extends BasicView {
    private final VerticalLayout content = new VerticalLayout();

    private final ModelService modelService;

    public ModelView(@Autowired ModelService modelService) {
        addRouterLinkToDrawer(new RouterLink("Формы", ModelView.class));
        addRouterLinkToDrawer(new RouterLink("Данные", DataView.class));
        this.modelService = modelService;
        content.setWidth("100%");
        content.setHeight("100%");
        setContent(content);
        createGrid();
    }

    private List<ColumnDefinition<Model, Criteria>> createColumns() {

        return Arrays.asList(
                ColumnDefinition.<Model, Criteria>builder().columnName("Id")
                        .getter(Model::getId)
                        .visible(true)
                        .build(),

                ColumnDefinition.<Model, Criteria>builder().columnName("Название")
                        .getter(Model::getName)
                        .sortable(true)
                        .sortProperty("name")
                        .filter((fieldValue) -> Criteria.where("name").regex(fieldValue))
                        .bind((modelBinder) -> {
                            TextField nameField = new TextField();
                            modelBinder.forField(nameField)
                                    .withValidator(new StringLengthValidator("Название должно быть от 3 до 50 символов.", 3, 50))
                                    .bind(Model::getName, Model::setName);
                            return nameField;
                        })
                        .editable(true)
                        .build(),

                ColumnDefinition.<Model, Criteria>builder().columnName("Автор")
                        .getter(e -> e.getAuthor() == null ? null : e.getAuthor().getLogin())
                        .build(),

                ColumnDefinition.<Model, Criteria>builder().columnName("Поля")
                        .renderer(new TextRenderer<>(e -> e.getModelColumnList() == null ? "0" :Integer.toString(e.getModelColumnList().size())))
                        .getter(Model::getModelColumnList)
                        .bind((modelBinder) -> {
                            DialogGrid<ModelColumn> modelColumnDialogGrid = createModelColumnDialogGrid();
                            modelBinder.forField(modelColumnDialogGrid)
                                    .withValidator(e -> e != null && e.size() != 0, "Объект должен содержать минимум 1 поле")
                                    .bind(e -> ModelColumn.duplicate(e.getModelColumnListAsArray()), Model::setModelColumnListFromArray);
                            return modelColumnDialogGrid;
                        })
                        .editable(true)
                        .build()
        );
    }

    private DialogGrid<ModelColumn> createModelColumnDialogGrid() {
        List<ColumnDefinition<ModelColumn,  Void>> columnDefinitions = Arrays.asList(
                ColumnDefinition.<ModelColumn, Void>builder()
                        .columnName("Название поля")
                        .getter(ModelColumn::getColumnName)
                        .bind((modelBinder) -> {
                            TextField nameField = new TextField();
                            modelBinder.forField(nameField)
                                    .withValidator(new StringLengthValidator("First name length must be between 3 and 50.", 3, 50))
                                    .bind(ModelColumn::getColumnName, ModelColumn::setColumnName);
                            return nameField;
                        })
                        .visible(true)
                        .editable(true)
                        .build(),

                ColumnDefinition.<ModelColumn, Void>builder()
                        .columnName("Тип поля")
                        .getter(ModelColumn::getColumnType)
                        .bind((modelBinder) -> {
                            ComboBox<ModelColumnType> comboBox = new ComboBox<>();
                            comboBox.setItems(ModelColumnType.String,  ModelColumnType.Number, ModelColumnType.Date, ModelColumnType.Base64);
                            modelBinder.forField(comboBox)
                                    .withValidator(Objects::nonNull, "Вы должны выбрать тип поля")
                                    .bind(ModelColumn::getColumnType, ModelColumn::setColumnType);
                            return comboBox;
                        })
                        .visible(true)
                        .editable(true)
                        .build()
        );

        return new DialogGrid<>(columnDefinitions, ModelColumn::new);
    }

    private void createGrid() {
        CrudGrid<Model, Criteria> grid = new CrudGrid<>(modelService, createColumns(),
                new MongodbQuery());
        content.add(grid);
    }

}
