package com.backend.shop.Controller;

import com.backend.shop.DataTransferObject.LoginDTO;
import com.backend.shop.Model.UserModel;
import com.backend.shop.Service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private final UserService userService;

    // Constructor injection for UserService dependency
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    // Display the login page
    @GetMapping("/login")
    public String showLoginForm(Model model) {

        // Add an empty LoginDTO object for form binding
        model.addAttribute("loginDTO", new LoginDTO());

        return "login"; // returns login.html
    }

    // Handle login form submission
    @PostMapping("/login")
    public String processLogin(
            @Valid @ModelAttribute("loginDTO") LoginDTO loginDTO, // Bind form data to LoginDTO
            BindingResult bindingResult,  // Holds validation errors, if any
            Model model,
            HttpSession session           // Session used to store logged-in user
    ) {

        // If validation fails, reload the login page
        if (bindingResult.hasErrors()) {
            return "login";
        }

        // Authenticate user based on email + password
        UserModel user = userService.authenticate(
                loginDTO.getEmail(),
                loginDTO.getPassword()
        );

        // If authentication fails, return error message
        if (user == null) {
            model.addAttribute("loginError", "Invalid email or password");
            return "login"; // reload login page
        }

        // Save authenticated user into session to maintain login state
        session.setAttribute("loggedInUser", user);

        // Add username to display in login_success.html
        model.addAttribute("username", user.getUsername());

        return "login_success"; // return login_success.html
    }

    // Logout endpoint — clears the session and redirects to login page
    @GetMapping("/logout")
    public String logout(HttpSession session) {

        // Invalidate the entire session (removes loggedInUser)
        session.invalidate();

        return "redirect:/login"; // redirect back to login page
    }
}
