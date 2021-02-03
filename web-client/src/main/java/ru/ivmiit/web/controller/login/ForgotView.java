package ru.ivmiit.web.controller.login;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.ivmiit.web.forms.ForgotForm;

import java.util.Optional;
import java.util.stream.Collectors;


@Route("forgot")
@PageTitle("Востановление пароля")
public class ForgotView extends VerticalLayout {

    private final Binder<ForgotForm> binder = new Binder<>();

    private final ForgotForm forgotForm = new ForgotForm();

    public ForgotView() {
        addClassName("login-view");
        setSizeFull();

        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        FormLayout formLayout = new FormLayout();
        fillForgotForm(formLayout);
        Div div = new Div();
        div.add(formLayout);
        add(new H1("Востановление пароля"), div);
    }


    protected void fillForgotForm(FormLayout formLayout) {

        TextField login = new TextField();
        login.setPlaceholder("Login");
        login.setLabel("Логин");
        login.setTitle("Введите логин для востановления");

        formLayout.add(login);

        Button saveButton = new Button();
        saveButton.setText("Востановить");

        HorizontalLayout actions = new HorizontalLayout();
        actions.add(saveButton);

        formLayout.add(actions);

        binder.forField(login)
                .withValidator(new StringLengthValidator("Пожалуйста введите логин", 3, 255))
                .bind(ForgotForm::getLogin, ForgotForm::setLogin);

        saveButton.addClickListener(event -> {
            if (binder.writeBeanIfValid(forgotForm)) {
                new Notification("Успешно: " + forgotForm.toString(), 5000).open();
            } else {
                BinderValidationStatus<ForgotForm> validate = binder.validate();
                String errorText = validate.getFieldValidationStatuses()
                        .stream().filter(BindingValidationStatus::isError)
                        .map(BindingValidationStatus::getMessage)
                        .map(Optional::get).distinct()
                        .collect(Collectors.joining(", "));
                new Notification("Ошибки: " + errorText, 5000).open();
            }
        });
    }
}
