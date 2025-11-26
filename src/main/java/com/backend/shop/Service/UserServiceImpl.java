package com.backend.shop.Service;

import com.backend.shop.DataTransferObject.RegisterDTO;
import com.backend.shop.Model.AdminModel;
import com.backend.shop.Model.CustomerModel;
import com.backend.shop.Model.SellerModel;
import com.backend.shop.Model.AuthenticatedUser;
import com.backend.shop.Repository.AdminRepository;
import com.backend.shop.Repository.CustomerRepository;
import com.backend.shop.Repository.SellerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(AdminRepository adminRepository,
                           CustomerRepository customerRepository,
                           SellerRepository sellerRepository,
                           PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.customerRepository = customerRepository;
        this.sellerRepository = sellerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ===== Register methods =====

    @Override
    @Transactional
    public AdminModel registerAdmin(RegisterDTO registerDTO) {

        // Email must be unique across all role tables
        if (emailExists(registerDTO.getEmail())) {
            return null;
        }

        AdminModel admin = new AdminModel();
        admin.setEmail(registerDTO.getEmail());
        admin.setUsername(registerDTO.getUsername());
        admin.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        adminRepository.save(admin);
        return admin;
    }

    @Override
    @Transactional
    public CustomerModel registerCustomer(RegisterDTO registerDTO) {

        if (emailExists(registerDTO.getEmail())) {
            return null;
        }

        CustomerModel customer = new CustomerModel();
        customer.setEmail(registerDTO.getEmail());
        customer.setUsername(registerDTO.getUsername());
        customer.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        customerRepository.save(customer);
        return customer;
    }

    @Override
    @Transactional
    public SellerModel registerSeller(RegisterDTO registerDTO) {

        if (emailExists(registerDTO.getEmail())) {
            return null;
        }

        SellerModel seller = new SellerModel();
        seller.setEmail(registerDTO.getEmail());
        seller.setUsername(registerDTO.getUsername());
        seller.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        sellerRepository.save(seller);
        return seller;
    }

    // ===== Login / Authenticate =====

    @Override
    public AuthenticatedUser authenticate(String email, String rawPassword) {

        // 1) Try admin table
        AdminModel admin = adminRepository.findByEmail(email);
        if (admin != null && passwordEncoder.matches(rawPassword, admin.getPassword())) {
            return new AuthenticatedUser(admin.getEmail(), admin.getUsername(), "ADMIN");
        }

        // 2) Try customer table
        CustomerModel customer = customerRepository.findByEmail(email);
        if (customer != null && passwordEncoder.matches(rawPassword, customer.getPassword())) {
            return new AuthenticatedUser(customer.getEmail(), customer.getUsername(), "CUSTOMER");
        }

        // 3) Try seller table
        SellerModel seller = sellerRepository.findByEmail(email);
        if (seller != null && passwordEncoder.matches(rawPassword, seller.getPassword())) {
            return new AuthenticatedUser(seller.getEmail(), seller.getUsername(), "SELLER");
        }

        // No match in any table
        return null;
    }

    // ===== Utility methods =====

    @Override
    public boolean emailExists(String email) {
        return adminRepository.exists(email)
                || customerRepository.exists(email)
                || sellerRepository.exists(email);
    }

    @Override
    public List<AuthenticatedUser> getAllUsersSummary() {
        List<AuthenticatedUser> list = new ArrayList<>();

        adminRepository.findAll().forEach(a ->
                list.add(new AuthenticatedUser(a.getEmail(), a.getUsername(), "ADMIN"))
        );
        customerRepository.findAll().forEach(c ->
                list.add(new AuthenticatedUser(c.getEmail(), c.getUsername(), "CUSTOMER"))
        );
        sellerRepository.findAll().forEach(s ->
                list.add(new AuthenticatedUser(s.getEmail(), s.getUsername(), "SELLER"))
        );

        return list;
    }

    @Override
    public boolean isAdmin(AuthenticatedUser user) {
        return user != null && "ADMIN".equals(user.getRole());
    }
}
