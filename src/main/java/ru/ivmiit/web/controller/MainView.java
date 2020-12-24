package ru.ivmiit.web.controller;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import ru.azat.vaadin.crud.common.BasicView;

@Route()
public class MainView extends BasicView {

    public MainView() {
        addRouterLinkToDrawer(new RouterLink("Формы", ModelView.class));
        addRouterLinkToDrawer(new RouterLink("Данные", DataView.class));
    }
}