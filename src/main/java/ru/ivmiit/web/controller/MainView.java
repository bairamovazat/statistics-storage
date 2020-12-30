package ru.ivmiit.web.controller;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;
import ru.azat.vaadin.crud.common.BasicView;
import ru.ivmiit.web.service.AuthenticationService;
import ru.ivmiit.web.utils.LinkUtils;

@Route()
public class MainView extends BasicView {

    public MainView(@Autowired AuthenticationService authenticationService) {
        addRouterLinkToDrawer(LinkUtils.getRouterLinksToCurrentUser(authenticationService.getCurrentUser()));
    }
}