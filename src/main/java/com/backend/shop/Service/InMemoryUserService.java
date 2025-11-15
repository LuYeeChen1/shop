package com.backend.shop.Service;

import com.backend.shop.DataTransferObject.RegisterDTO;
import com.backend.shop.Model.UserModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InMemoryUserService implements UserService {

    // This list works as an in-memory database
    private final List<UserModel> users = new ArrayList<>();

    @Override
    public UserModel registerNewUser(RegisterDTO registerDTO) {
        UserModel userModel = new UserModel(
                registerDTO.getUsername(),
                registerDTO.getEmail(),
                registerDTO.getPassword()
        );

        users.add(userModel);
        return userModel;
    }

    @Override
    public List<UserModel> getAllUsers() {
        return users;
    }

    @Override
    public UserModel authenticate(String email, String password) {
        // Simple linear search for demo purposes
        for (UserModel user : users) {
            if (user.getEmail().equalsIgnoreCase(email)
                    && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}
