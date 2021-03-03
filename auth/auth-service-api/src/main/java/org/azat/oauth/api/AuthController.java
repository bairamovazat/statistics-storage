package org.azat.oauth.api;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

public interface AuthController {

    @GetMapping("/token")
    public String getToken(@RequestParam("code") String code);

    @PostMapping("/validate")
    public Boolean validateToken(@RequestParam("token") String token);

    @GetMapping("/refresh")
    public String refresh(HttpServletRequest req);

}
