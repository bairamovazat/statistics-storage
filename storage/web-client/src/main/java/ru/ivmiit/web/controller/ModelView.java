package ru.ivmiit.web.controller;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.data.validator.StringLengthValidator;
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
import ru.azat.vaadin.crud.common.DialogGrid;
import ru.itis.storage.api.ModelService;
import ru.itis.storage.api.MongodbQuery;
import ru.itis.storage.api.model.Model;
import ru.itis.storage.api.model.ModelColumn;
import ru.itis.storage.api.model.ModelColumnType;
import ru.ivmiit.web.client.MainClientImpl;
import ru.ivmiit.web.utils.LinkUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Route("model")
@Secured("CREATOR")
@EnableFeignClients
public class ModelView extends BasicView {
    private final VerticalLayout content = new VerticalLayout();

    private final MainClientImpl mainClient;

    public ModelView(@Autowired MainClientImpl mainClient) {

        this.mainClient = mainClient;

        addRouterLinkToDrawer(LinkUtils.getRouterLinks());

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

//                ColumnDefinition.<Model, Criteria>builder().columnName("Автор")
//                        .getter(e -> e.getAuthor() == null ? null : e.getAuthor().getLogin())
//                        .build(),

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
        CrudGrid<Model, Criteria> grid = new CrudGrid<>(getModelService(), createColumns(),
                new MongodbQuery(), true);
        content.add(grid);
    }

    private ModelService getModelService() {
        return new ModelService() {
            @Override
            public List<Model> getAll() {
                return mainClient.getAllModel();
            }

            @Override
            public Model createNew() {
                return mainClient.createNewModel();
            }

            @Override
            public Model create(Model element) {
                return mainClient.createModel(element);
            }

            @Override
            public Model update(Model element) {
                return mainClient.updateModel(element);
            }

            @Override
            public void delete(Model element) {
                mainClient.deleteModel(element);
            }

            @Override
            public List<Model> readAll() {
                return mainClient.readAllModel();
            }

            @Override
            public int count() {
                return mainClient.countModel();
            }

            @Override
            public int count(Optional<Query<Criteria>> query) {
                return mainClient.countModel(query);
            }

            @Override
            public List<Model> load(int offset, int limit) {
                return mainClient.loadModel(offset, limit);
            }

            @Override
            public List<Model> load(int offset, int limit, LoadMultipleObject<Criteria> loadMultipleObject) {
                return mainClient.loadModel(offset, limit, loadMultipleObject);
            }
        };
    }

}
