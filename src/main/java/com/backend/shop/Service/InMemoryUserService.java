package com.backend.shop.Service;

import com.backend.shop.DataTransferObject.RegisterDTO;
import com.backend.shop.Model.UserModel;
import com.backend.shop.Model.UserRole;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InMemoryUserService implements UserService {

    private final List<UserModel> users = new ArrayList<>();

    public InMemoryUserService() {
        // No default admin created here.
    }

    @Override
    public UserModel registerNewUser(RegisterDTO registerDTO) {
        return registerNewUser(registerDTO, UserRole.CUSTOMER);
    }

    @Override
    public UserModel registerNewUser(RegisterDTO registerDTO, UserRole role) {

        if (emailExists(registerDTO.getEmail())) {
            return null;
        }

        // Create new user
        UserModel userModel = new UserModel(
                registerDTO.getUsername(),
                registerDTO.getEmail(),
                registerDTO.getPassword()
        );

        userModel.setId(generateUserId());

        // Set role
        userModel.setUserRole(role);

        // Always default seller application status to NONE
        userModel.setSellerApplicationStatus("NONE");

        // Add to in-memory list
        users.add(userModel);

        return userModel;
    }

    @Override
    public List<UserModel> getAllUsers() {
        return users;
    }

    @Override
    public UserModel authenticate(String email, String password) {
        for (UserModel user : users) {
            if (user.getEmail().equalsIgnoreCase(email)
                    && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public boolean emailExists(String email) {
        return findByEmail(email) != null;
    }

    @Override
    public boolean isAdmin(UserModel user) {
        return user != null && user.getUserRole() == UserRole.ADMIN;
    }

    @Override
    public UserModel changeUserRole(UserModel user, UserRole newRole) {
        if (user == null) {
            return null;
        }
        user.setUserRole(newRole);
        return user;
    }

    @Override
    public UserModel findByEmail(String email) {
        for (UserModel user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public UserModel findById(Long id) {
        for (UserModel user : users) {
            if (user.getId() != null && user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    private Long generateUserId() {
        return (long) (users.size() + 1);
    }
}
