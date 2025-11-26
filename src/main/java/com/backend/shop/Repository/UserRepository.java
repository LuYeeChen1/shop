package com.backend.shop.Repository;

import com.backend.shop.Model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

// The primary key type is String because email is the @Id
public interface UserRepository extends JpaRepository<UserModel, String> {

    // Check if a user exists by email
    boolean existsByEmail(String email);

    // Find a user by email
    UserModel findByEmail(String email);
}
