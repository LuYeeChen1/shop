package com.backend.shop.Utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Scanner;

public class BCryptGenerator {

    public static void main(String[] args) {

        // Scanner for reading user input from console
        Scanner scanner = new Scanner(System.in);

        System.out.println("====================================");
        System.out.println("  BCrypt Password Hash Generator");
        System.out.println("====================================");
        System.out.print("Enter password to encrypt: ");

        // Read raw password from console
        String rawPassword = scanner.nextLine();

        // Create a BCrypt encoder
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Generate the BCrypt hash
        String hashed = encoder.encode(rawPassword);

        System.out.println("\n------------------------------------");
        System.out.println(" Raw password : " + rawPassword);
        System.out.println(" BCrypt hash  : ");
        System.out.println(hashed);
        System.out.println("------------------------------------");
        System.out.println("Copy the hash and insert into MySQL.");
        System.out.println("====================================");

        scanner.close();
    }
}
