package ru.ivmiit.web.controller;

import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.azat.vaadin.crud.common.BasicView;
import ru.ivmiit.web.client.GreetingClient;
import ru.ivmiit.web.utils.LinkUtils;

@Route()
@EnableFeignClients
public class MainView extends BasicView {

    public MainView(@Autowired GreetingClient greetingClient) {
        addRouterLinkToDrawer(LinkUtils.getRouterLinks());
        System.out.println(greetingClient.greeting());
    }
}