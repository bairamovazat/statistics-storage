package ru.ivmiit.web.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.itis.storage.api.MainClient;

@FeignClient("spring-cloud-eureka-client")
public interface MainClientImpl extends MainClient {
}
