package com.backend.shop.Service.Interface;

import com.backend.shop.Model.UserModel;

import java.util.List;

public interface UserAdminService {

    boolean isLoggedIn(UserModel user);

    boolean canViewUserList(UserModel user);

    List<UserModel> getAllUsersForAdmin();
}
