package com.backend.shop.Controller;

import com.backend.shop.DataTransferObject.RegisterDTO;
import com.backend.shop.Model.UserModel;
import com.backend.shop.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(
            @Valid @ModelAttribute("registerDTO") RegisterDTO registerDTO,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        // Use default role (CUSTOMER) from service
        UserModel createdUser = userService.registerNewUser(registerDTO);

        if (createdUser == null) {
            model.addAttribute("registerError", "Email is already registered.");
            return "register";
        }

        model.addAttribute("registeredUser", createdUser);
        return "register_success";
    }
}
