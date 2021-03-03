package murraco.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {


    @GetMapping("/signin_page")
    public String getLoginPage(Model model, @RequestParam("redirect") String redirectUrl) {
        return "login";
    }
}
