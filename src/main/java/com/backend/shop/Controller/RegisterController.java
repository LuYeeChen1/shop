package com.backend.shop.Controller;

import com.backend.shop.DataTransferObject.RegisterDTO;
import com.backend.shop.Model.UserModel;
import com.backend.shop.Service.RegisterService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

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

        // Delegate registration logic to RegisterService
        UserModel createdUser = registerService.register(registerDTO);

        if (createdUser == null) {
            // Email is already registered (handled by RegisterService)
            model.addAttribute("registerError", "Email is already registered.");
            return "register";
        }

        //createdUsername Showed in register_success.html
        model.addAttribute("createdUsername", createdUser.getUsername());
        return "register_success";
    }
}
