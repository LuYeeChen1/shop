package com.backend.shop.Controller;

import com.backend.shop.DataTransferObject.RegisterDTO;
import com.backend.shop.Model.UserModel;
import com.backend.shop.Service.UserService;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpSession;   // Required for session-based authentication
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    private final UserService userService;

    // Constructor injection of UserService
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    // Display the welcome page (supports "/" and "/welcome")
    @GetMapping({"/", "/welcome"})
    public String welcome() {
        return "welcome"; // returns welcome.html
    }

    // Display the registration form
    @GetMapping("/register")
    public String showRegisterForm(Model model) {

        // Add an empty RegisterDTO object for form binding in Thymeleaf
        model.addAttribute("registerDTO", new RegisterDTO());

        return "register"; // return register.html
    }

    // Handle form submission from the registration page
    @PostMapping("/register")
    public String processRegister(
            @Valid @ModelAttribute("registerDTO") RegisterDTO registerDTO, // Bind form data to DTO
            BindingResult bindingResult, // Holds validation results
            Model model
    ) {
        // If validation errors exist, reload the form
        if (bindingResult.hasErrors()) {
            return "register";
        }

        // Pass DTO to the service layer to create a new user
        UserModel userModel = userService.registerNewUser(registerDTO);

        // Add username to model so success page can display it
        model.addAttribute("username", userModel.getUsername());

        return "register_success"; // returns register_success.html
    }

    // This page should only be accessible if the user is logged in
    @GetMapping("/users")
    public String showUsers(Model model, HttpSession session) {

        // Retrieve the logged-in user from session
        UserModel loggedInUser = (UserModel) session.getAttribute("loggedInUser");

        // If no session found → redirect to login page
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // Load all registered users and pass to the view
        model.addAttribute("users", userService.getAllUsers());

        return "user_list"; // returns user_list.html
    }
}
