package com.backend.shop.Controller;

import com.backend.shop.DataTransferObject.LoginDTO;
import com.backend.shop.Model.UserModel;
import com.backend.shop.Model.UserRole;
import com.backend.shop.Service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(
            @Valid @ModelAttribute("loginDTO") LoginDTO loginDTO,
            BindingResult bindingResult,
            Model model,
            HttpSession session
    ) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        UserModel user = userService.authenticate(loginDTO.getEmail(), loginDTO.getPassword());

        if (user == null) {
            model.addAttribute("loginError", "Invalid email or password");
            return "login";
        }

        session.setAttribute("loggedInUser", user);

        if (user.getUserRole() == UserRole.ADMIN) {
            return "redirect:/admin/dashboard";
        } else if (user.getUserRole() == UserRole.AGENT) {
            return "redirect:/agent/dashboard";
        } else if (user.getUserRole() == UserRole.SELLER) {
            return "redirect:/seller/dashboard";
        } else {
            return "redirect:/customer/dashboard";
        }
    }
}
