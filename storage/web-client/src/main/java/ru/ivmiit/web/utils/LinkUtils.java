package ru.ivmiit.web.utils;

import com.vaadin.flow.router.RouterLink;
import ru.ivmiit.web.controller.DataView;
import ru.ivmiit.web.controller.ModelView;
import ru.ivmiit.web.controller.UserView;
import ru.ivmiit.web.model.User;
import ru.ivmiit.web.security.details.Role;

import java.util.ArrayList;
import java.util.List;

public class LinkUtils {

    private LinkUtils() {

    }

    public static RouterLink[] getRouterLinksToCurrentUser(User user) {
        List<RouterLink> routerLinkList = new ArrayList<>();
        if (user.getRoles().contains(Role.ADMIN)) {
            routerLinkList.add(new RouterLink("Пользователи", UserView.class));
        }

        if (user.getRoles().contains(Role.CREATOR)) {
            routerLinkList.add(new RouterLink("Формы", ModelView.class));
            routerLinkList.add(new RouterLink("Данные", DataView.class));
        } else if (user.getRoles().contains(Role.USER)) {
            routerLinkList.add(new RouterLink("Данные", DataView.class));
        }

        return routerLinkList.toArray(new RouterLink[0]);
    }
}
