package com.backend.shop.Controller.ErrorController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController {

    @GetMapping("/error/404")
    public String notFoundError() {
        return "error-pages/404";
    }

    @GetMapping("/error/500")
    public String internalServerError() {
        return "error-pages/500";
    }

    @GetMapping("/error/default")
    public String defaultError() {
        return "error-pages/error";
    }
}
