package ru.ivmiit.web.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.itis.storage.api.GreetingController;

@FeignClient("spring-cloud-eureka-client")
public interface GreetingClient extends GreetingController {
}
