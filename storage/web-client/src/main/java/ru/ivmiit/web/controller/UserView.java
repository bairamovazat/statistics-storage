package ru.ivmiit.web.controller;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.access.annotation.Secured;
import ru.azat.vaadin.crud.common.BasicView;
import ru.azat.vaadin.crud.common.ColumnDefinition;
import ru.azat.vaadin.crud.common.CrudGrid;
import ru.azat.vaadin.crud.common.DialogGrid;
import ru.ivmiit.web.model.Model;
import ru.ivmiit.web.model.ModelColumn;
import ru.ivmiit.web.model.ModelColumnType;
import ru.ivmiit.web.model.User;
import ru.ivmiit.web.repository.MongodbQuery;
import ru.ivmiit.web.security.details.Role;
import ru.ivmiit.web.security.details.RoleWrapper;
import ru.ivmiit.web.service.*;
import ru.ivmiit.web.utils.LinkUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Route("user")
@Secured("ADMIN")
public class UserView extends BasicView {

    private final VerticalLayout content = new VerticalLayout();

    private final UserService userService;

    private final RegistrationService registrationService;
    private final AuthenticationService authenticationService;

    public UserView(@Autowired UserService userService,
                    @Autowired RegistrationService registrationService,
                    @Autowired AuthenticationService authenticationService
    ) {

        this.userService = userService;
        this.registrationService = registrationService;
        this.authenticationService = authenticationService;

        addRouterLinkToDrawer(LinkUtils.getRouterLinksToCurrentUser(authenticationService.getCurrentUser()));

        content.setWidth("100%");
        content.setHeight("100%");
        setContent(content);
        createGrid();
    }

    private List<ColumnDefinition<User, Criteria>> createColumns() {

        return Arrays.asList(
                ColumnDefinition.<User, Criteria>builder().columnName("Id")
                        .getter(User::getId)
                        .visible(true)
                        .build(),

                ColumnDefinition.<User, Criteria>builder().columnName("Логин")
                        .getter(User::getLogin)
                        .sortable(true)
                        .sortProperty("login")
                        .filter((fieldValue) -> Criteria.where("login").regex(fieldValue))
                        .bind((modelBinder) -> {
                            TextField nameField = new TextField();
                            modelBinder.forField(nameField)
                                    .withValidator(new StringLengthValidator("Логин должен быть от 3 до 50 символов.", 3, 50))
                                    .bind(User::getLogin, User::setLogin);
                            return nameField;
                        })
                        .editable(true)
                        .build(),

                ColumnDefinition.<User, Criteria>builder().columnName("Email")
                        .getter(User::getEmail)
                        .sortable(true)
                        .sortProperty("email")
                        .filter((fieldValue) -> Criteria.where("email").regex(fieldValue))
                        .bind((modelBinder) -> {
                            TextField nameField = new TextField();
                            modelBinder.forField(nameField)
                                    .withValidator(new EmailValidator("Укажите правильный Email"))
                                    .bind(User::getEmail, User::setEmail);
                            return nameField;
                        })
                        .editable(true)
                        .build(),

                ColumnDefinition.<User, Criteria>builder().columnName("Роли")
//                        .renderer(new TextRenderer<>(e -> e.getModelColumnList() == null ? "0" :Integer.toString(e.getModelColumnList().size())))
                        .getter(User::getRoles)
                        .bind((modelBinder) -> {
                            DialogGrid<RoleWrapper> modelColumnDialogGrid = createModelColumnDialogGrid();
                            modelBinder.forField(modelColumnDialogGrid)
                                    .withValidator(e -> e != null && e.size() != 0, "Объект должен содержать минимум 1 поле")
                                    .bind(e -> e.getRoles().stream().map(RoleWrapper::new).collect(Collectors.toList()),
                                            (e, v) -> e.setRoles(v.stream().map(RoleWrapper::getRole).collect(Collectors.toList())));
                            return modelColumnDialogGrid;
                        })
                        .editable(true)
                        .build()
        );
    }

    private DialogGrid<RoleWrapper> createModelColumnDialogGrid() {
        List<ColumnDefinition<RoleWrapper,  Void>> columnDefinitions = Arrays.asList(
                ColumnDefinition.<RoleWrapper, Void>builder()
                        .columnName("Роль")
                        .renderer(new TextRenderer<>(e -> e.getRole().toString()))
                        .getter(e -> e)
                        .bind((modelBinder) -> {
                            ComboBox<Role> comboBox = new ComboBox<>();
                            comboBox.setItems(Role.values());
                            modelBinder.forField(comboBox)
                                    .withValidator(Objects::nonNull, "Роль")
                                    .bind(RoleWrapper::getRole, RoleWrapper::setRole);
                            return comboBox;
                        })
                        .visible(true)
                        .editable(true)
                        .build()
        );

        return new DialogGrid<>(columnDefinitions, RoleWrapper::new);
    }

    private void createGrid() {
        CrudGrid<User, Criteria> grid = new CrudGrid<>(userService, createColumns(),
                new MongodbQuery());
        content.add(grid);
    }

}
