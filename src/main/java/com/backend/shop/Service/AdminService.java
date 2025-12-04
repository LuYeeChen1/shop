package com.backend.shop.Service;

import com.backend.shop.Model.Admin.AdminModel;
import com.backend.shop.Repository.Admin.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository,
                        PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AdminModel authenticate(String username, String rawPassword) {
        AdminModel admin = adminRepository.findByUsername(username);
        if (admin == null) {
            return null;
        }

        boolean matches = passwordEncoder.matches(rawPassword, admin.getPassword());
        if (!matches) {
            return null;
        }

        return admin;
    }
}
