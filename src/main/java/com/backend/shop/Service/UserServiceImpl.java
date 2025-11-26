package com.backend.shop.Service;

import com.backend.shop.DataTransferObject.RegisterDTO;
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
    private final PasswordEncoder passwordEncoder; // Add encoder field

    // Inject repository and password encoder
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserModel registerNewUser(RegisterDTO registerDTO) {

        // Check if email already exists in database
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            // Email already registered, return null to indicate failure
            return null;
        }

        // Map DTO to entity
        UserModel userModel = new UserModel();
        userModel.setEmail(registerDTO.getEmail());
        userModel.setUsername(registerDTO.getUsername());

        // Encrypt the raw password before saving
        // NEVER store plain text passwords in the database
        String encodedPassword = passwordEncoder.encode(registerDTO.getPassword());
        userModel.setPassword(encodedPassword);

        userModel.setRole(UserRole.CUSTOMER);

        // Save user to database
        return userRepository.save(userModel);
    }


    @Override
    public UserModel authenticate(String email, String rawPassword) {
        // Find user by email
        UserModel user = userRepository.findByEmail(email);

        if (user == null) {
            // User not found
            return null;
        }

        // Check if raw password matches the encoded password in database
        // passwordEncoder.matches() handles BCrypt hash comparison safely
        boolean passwordMatches = passwordEncoder.matches(rawPassword, user.getPassword());

        if (passwordMatches) {
            return user;
        }

        // Password does not match
        return null;
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean isAdmin(UserModel user) {
        return user != null && user.getRole() == UserRole.ADMIN;
    }
}

