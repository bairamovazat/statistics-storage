package ru.ivmiit.web.controller.login;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ivmiit.web.forms.UserRegistrationForm;
import ru.ivmiit.web.service.RegistrationService;

import java.util.Optional;
import java.util.stream.Collectors;

@Route("register")
@PageTitle("Востановление пароля")
public class RegisterView extends VerticalLayout {

    private Binder<UserRegistrationForm> binder = new Binder<>();

    private FormLayout formLayout = new FormLayout();

    private UserRegistrationForm registrationForm = new UserRegistrationForm();

    @Autowired
    private RegistrationService registrationService;

    public RegisterView() {

        addClassName("login-view");
        setSizeFull();

        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setAlignItems(FlexComponent.Alignment.CENTER);
        formLayout.setMaxWidth("400px");
        fillRegisterForm(formLayout);
        Div div = new Div();
        div.add(formLayout);
        add(new H1("Регистрация"), div);
    }


    protected void fillRegisterForm(FormLayout formLayout) {

        //-----Логин-----//
        TextField login = new TextField();
        login.setPlaceholder("login");
        login.setLabel("Логин");
        login.setTitle("Введите логин");
        formLayout.add(login);
        //-----Логин-----//

        //-----Пароль-----//
        PasswordField password = new PasswordField();
        password.setPlaceholder("password");
        password.setLabel("Пароль");
        password.setTitle("Введите пароль");
        formLayout.add(password);
        //-----Пароль-----//

        //-----email-----//
        TextField email = new TextField();
        email.setPlaceholder("mail@mail.ru");
        email.setLabel("Почта");
        email.setTitle("Введите почту");
        formLayout.add(email);
        //-----email-----//

        Button saveButton = new Button();
        saveButton.setText("Регистрация");

        HorizontalLayout actions = new HorizontalLayout();
        actions.add(saveButton);

        formLayout.add(actions);

//        binder.forField(login)
//                .withValidator(value -> login.getValue() != null && !login.getValue().trim().isEmpty(),
//                        "Логин не может быть пустым")
//                .bind(UserRegistrationForm::getLogin, UserRegistrationForm::setLogin);

        binder.forField(login)
                .withValidator(e -> !registrationService.userIsPresent(login.getValue()), "Пользователь с таким логином уже существует")
                .withValidator(new StringLengthValidator("Пожалуйста введите логин", 3, 255))
                .bind(UserRegistrationForm::getLogin, UserRegistrationForm::setLogin);

        binder.forField(password)
                .withValidator(new StringLengthValidator("Пожалуйста введите пароль", 3, 255))
                .bind(UserRegistrationForm::getPassword, UserRegistrationForm::setPassword);

        binder.forField(email)
                .withValidator(new EmailValidator("Пожалуйста введите почту"))
                .bind(UserRegistrationForm::getEmail, UserRegistrationForm::setEmail);

        saveButton.addClickListener(event -> {
            if (binder.writeBeanIfValid(registrationForm)) {
                registrationService.register(registrationForm);
                this.getUI().ifPresent(ui -> ui.navigate(LoginView.class));
            } else {
                BinderValidationStatus<UserRegistrationForm> validate = binder.validate();
                String errorText = validate.getFieldValidationStatuses()
                        .stream().filter(BindingValidationStatus::isError)
                        .map(BindingValidationStatus::getMessage)
                        .map(Optional::get).distinct()
                        .collect(Collectors.joining(", "));
                new Notification("Заполните форму!", 10000).open();
            }
        });
    }
}
