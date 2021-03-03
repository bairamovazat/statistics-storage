package ru.ivmiit.web.client;

import org.azat.oauth.api.AuthController;
import org.springframework.cloud.openfeign.FeignClient;
import ru.itis.storage.api.GreetingController;

@FeignClient("authorization-service")
public interface AuthClient extends AuthController {

}
