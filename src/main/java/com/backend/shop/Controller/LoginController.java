package com.backend.shop.Controller;

import com.backend.shop.DataTransferObject.LoginDTO;
import com.backend.shop.Model.UserModel;
import com.backend.shop.Service.LoginService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

//Controller Is Only A Gateway Or Security Guard. Not Brain

@Controller
public class LoginController {

    private final LoginService loginService;

    // Constructor injection
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    // Display login form
    @GetMapping("/login")
    public String showLoginForm(
            Model model,
            @RequestParam(value = "needLogin", required = false) String needLogin
    ) {
        model.addAttribute("loginDTO", new LoginDTO());

        if ("true".equals(needLogin)) {
            model.addAttribute("infoMessage", "Please log in before accessing that page.");
        }

        return "login";
    }

    // Process login submission
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

        // Delegate login logic to LoginService
        UserModel user = loginService.login(loginDTO);

        if (user == null) {
            model.addAttribute("loginError", "Invalid email or password");
            return "login";
        }

        // Save user in session (simple login session)
        session.setAttribute("loggedInUser", user);

        model.addAttribute("username", user.getUsername());
        return "login_success";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
