package com.backend.shop.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    // Handle requests for both "/" (root) and "/welcome"
    @GetMapping({"/", "/welcome"})
    public String welcome() {

        // Return the welcome.html page located in templates/
        // This page usually acts as a landing page for the application
        return "welcome";
    }
}
