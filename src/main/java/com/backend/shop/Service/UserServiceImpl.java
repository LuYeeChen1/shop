package com.backend.shop.Service;

import com.backend.shop.DataTransferObject.RegisterDTO;
import com.backend.shop.Model.UserModel;
import com.backend.shop.Model.UserRole;
import com.backend.shop.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserModel registerNewUser(RegisterDTO registerDTO) {

        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            return null; // duplicate email
        }

        UserModel user = new UserModel();
        user.setEmail(registerDTO.getEmail());
        user.setUsername(registerDTO.getUsername());
        user.setPassword(registerDTO.getPassword());
        user.setRole(UserRole.CUSTOMER);

        return userRepository.save(user);
    }

    @Override
    public UserModel authenticate(String email, String password) {
        UserModel user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
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

