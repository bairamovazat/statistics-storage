package ru.ivmiit.web.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;
import ru.ivmiit.web.controller.login.ForgotView;
import ru.ivmiit.web.controller.login.LoginView;
import ru.ivmiit.web.controller.login.RegisterView;

import java.util.Arrays;

@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

    private final static Class<?>[] UNAUTHORIZED_CLASS = {ForgotView.class, LoginView.class, RegisterView.class};
    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> {
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::beforeEnter);
        });
    }

    /**
     * Reroutes the user if they're not authorized to access the view.
     *
     * @param event before navigation event with event details
     */
    private void beforeEnter(BeforeEnterEvent event) {
        if (Arrays.stream(UNAUTHORIZED_CLASS).noneMatch(e -> e.equals(event.getNavigationTarget()))
                && !SecurityUtils.isUserLoggedIn()) {
            event.rerouteTo(LoginView.class);
        }

        if(!SecurityUtils.isAccessGranted(event.getNavigationTarget())) { //
            if(SecurityUtils.isUserLoggedIn()) { //
                event.rerouteToError(NotFoundException.class); //
            } else {
                event.rerouteTo(LoginView.class); //
            }
        }

    }
}