package murraco.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.netflix.discovery.EurekaClient;
import org.azat.oauth.api.AuthController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import murraco.service.UserService;

import java.io.IOException;

@RestController
@RequestMapping
public class UserControllerImpl implements AuthController {

    @Autowired
    private UserService userService;


    @Qualifier("eurekaClient")
    @Autowired
    @Lazy
    private EurekaClient eurekaClient;

    @GetMapping("/login")
    @ApiOperation(value = "${UserController.signin}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public String login(@RequestParam("username") String username, @RequestParam("password") String password,
                        @RequestParam("redirect") String redirect, HttpServletResponse response) throws IOException {
        String code =  userService.signin(username, password);
        response.sendRedirect(redirect + "?code=" + code);
        return code;
    }

    @Override
    @GetMapping("/token")
    @ApiOperation(value = "${UserController.token}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public String getToken(@RequestParam("code") String code) {
        return userService.getToken(code);
    }

    @Override
    @PostMapping("/validate")
    public Boolean validateToken(@RequestParam("token") String token) {
        return userService.validate(token);
    }


    @Override
    @GetMapping("/refresh")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public String refresh(HttpServletRequest req) {
        return userService.refresh(req.getRemoteUser());
    }

}
