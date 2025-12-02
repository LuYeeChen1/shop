package com.backend.shop.Controller.AdminController;

import com.backend.shop.Model.Admin.AdminModel;
import com.backend.shop.Model.Session.AuthenticatedUser;
import com.backend.shop.Service.AdminService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminLoginController {

    private final AdminService adminService;

    public AdminLoginController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admin/login")
    public String showAdminLoginForm(Model model) {
        return "admin_login";
    }

    @PostMapping("/admin/login")
    public String processAdminLogin(
            @RequestParam("username") @NotBlank String username,
            @RequestParam("password") @NotBlank String password,
            HttpSession session,
            Model model
    ) {
        AdminModel admin = adminService.authenticate(username, password);

        if (admin == null) {
            model.addAttribute("loginError", "Invalid username or password");
            return "admin_login";
        }

        // Reuse AuthenticatedUser as a safe session object
        AuthenticatedUser authAdmin = new AuthenticatedUser(
                admin.getAdminId(),
                admin.getContactEmail(),
                admin.getUsername(),
                "ADMIN"
        );

        session.setAttribute("loggedInAdmin", authAdmin);

        return "redirect:/admin/dashboard";
    }
}
