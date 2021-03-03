package ru.ivmiit.web.utils;

import com.vaadin.flow.router.RouterLink;
import ru.ivmiit.web.controller.DataView;
import ru.ivmiit.web.controller.ModelView;

import java.util.ArrayList;
import java.util.List;

public class LinkUtils {

    private LinkUtils() {

    }

    public static RouterLink[] getRouterLinks() {
        List<RouterLink> routerLinkList = new ArrayList<>();

        routerLinkList.add(new RouterLink("Формы", ModelView.class));
        routerLinkList.add(new RouterLink("Данные", DataView.class));


        return routerLinkList.toArray(new RouterLink[0]);
    }
}
