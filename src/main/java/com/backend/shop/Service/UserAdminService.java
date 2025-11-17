package com.backend.shop.Service;

import com.backend.shop.Model.UserModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAdminService {

    private final UserService userService;

    public UserAdminService(UserService userService) {
        this.userService = userService;
    }

    public boolean isLoggedIn(UserModel user) {
        return user != null;
    }

    public boolean canViewUserList(UserModel user) {
        return userService.isAdmin(user);
    }

    public List<UserModel> getAllUsersForAdmin() {
        return userService.getAllUsers();
    }
}
