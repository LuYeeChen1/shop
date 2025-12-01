package com.backend.shop.Service;

import com.backend.shop.DataTransferObject.RegisterDTO;
import com.backend.shop.Model.AuthenticatedUser;
import com.backend.shop.Model.UserModel;
import com.backend.shop.Model.UserRole;
import com.backend.shop.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ============================================================
    // REGISTER
    // ============================================================

    @Override
    @Transactional
    public UserModel registerNewUser(RegisterDTO registerDTO) {
        return registerNewUser(registerDTO, UserRole.CUSTOMER);
    }

    @Override
    @Transactional
    public UserModel registerNewUser(RegisterDTO registerDTO, UserRole role) {

        // Check email uniqueness
        if (emailExists(registerDTO.getEmail())) {
            return null;
        }

        // Hash password
        String encodedPassword = passwordEncoder.encode(registerDTO.getPassword());

        // Create user in users table
        Long userId = userRepository.createUser(
                registerDTO.getEmail(),
                encodedPassword,
                registerDTO.getUsername(),
                role.name()
        );

        // Return created user
        return userRepository.findById(userId);
    }

    // ============================================================
    // AUTHENTICATION (ENTERPRISE-LEVEL)
    // ============================================================

    @Override
    public AuthenticatedUser authenticate(String email, String rawPassword) {

        UserModel user = userRepository.findByEmail(email);
        if (user == null) {
            return null;
        }

        boolean passwordMatch = passwordEncoder.matches(rawPassword, user.getPassword());
        if (!passwordMatch) {
            return null;
        }

        // Return safe object for session
        return new AuthenticatedUser(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRole().name()
        );
    }

    // ============================================================
    // UTILITIES
    // ============================================================

    @Override
    public boolean emailExists(String email) {
        return userRepository.emailExists(email);
    }

    @Override
    public boolean isAdmin(UserModel user) {
        return user != null && user.getRole() == UserRole.ADMIN;
    }

    @Override
    @Transactional
    public UserModel changeUserRole(Long userId, UserRole newRole) {

        UserModel existing = userRepository.findById(userId);
        if (existing == null) {
            return null;
        }

        userRepository.updateUserRole(userId, newRole.name());
        existing.setRole(newRole);

        return existing;
    }

    @Override
    public UserModel findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserModel findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }
}
