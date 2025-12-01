package com.backend.shop.Service;

import com.backend.shop.DataTransferObject.RegisterDTO;
import com.backend.shop.Model.UserModel;
import com.backend.shop.Model.UserRole;

import java.util.List;

public interface UserService {

    UserModel registerNewUser(RegisterDTO registerDTO);

    UserModel registerNewUser(RegisterDTO registerDTO, UserRole role);

    List<UserModel> getAllUsers();

    UserModel authenticate(String email, String password);

    boolean emailExists(String email);

    boolean isAdmin(UserModel user);

    UserModel changeUserRole(UserModel user, UserRole newRole);

    UserModel findByEmail(String email);

    UserModel findById(Long id);
}
