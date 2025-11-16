package com.backend.shop.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessDeniedController {

    @GetMapping("/access-denied")
    public String showAccessDeniedPage() {
        return "access-denied"; // this is the HTML page name
    }
}
