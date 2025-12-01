package com.backend.shop.Controller;

import com.backend.shop.Model.UserModel;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/welcome";
    }

    @GetMapping("/welcome")
    public String welcome(HttpSession session, Model model) {
        UserModel user = (UserModel) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", user);
        return "welcome";
    }
}
