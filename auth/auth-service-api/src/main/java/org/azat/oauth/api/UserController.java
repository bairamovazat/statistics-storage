package org.azat.oauth.api;

import org.azat.oauth.dto.UserDataDTO;
import org.azat.oauth.dto.UserResponseDTO;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

public interface UserController {

    @GetMapping("/signin")
    public String login (@RequestParam String username, @RequestParam String password);

    @GetMapping("/token")
    public String getToken(@RequestParam String code);

    @PostMapping("/signup")
    public String signup(@RequestBody UserDataDTO user);

    @DeleteMapping(value = "/{username}")
    public String delete(@PathVariable String username);

    @GetMapping(value = "/{username}")
    public UserResponseDTO search(@PathVariable String username);

    @GetMapping(value = "/me")
    public UserResponseDTO whoami(HttpServletRequest req);

    @GetMapping("/refresh")
    public String refresh(HttpServletRequest req);


}
