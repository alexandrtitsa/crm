package ua.com.astone.acrm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home"; // Відображає home.html із фрагментом layout
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // Відображає login.html
    }
}
