package com.hostelmanagement.hostel_management.config;

import com.hostelmanagement.hostel_management.entity.Hostel;
import com.hostelmanagement.hostel_management.entity.User;
import com.hostelmanagement.hostel_management.enums.Role;
import com.hostelmanagement.hostel_management.repository.HostelRepository;
import com.hostelmanagement.hostel_management.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeData(
            UserRepository userRepository,
            HostelRepository hostelRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            // =========================
            // Verified test student
            // =========================

            if (userRepository
                    .findByEmail("oldstudent@test.com")
                    .isEmpty()) {

                User user = new User();

                user.setName("Old Student");
                user.setEmail("oldstudent@test.com");
                user.setMobile("9876543210");

                user.setPassword(
                        passwordEncoder.encode(
                                "OldStudent@123"
                        )
                );

                user.setRole(
                        Role.VERIFIED_OLD_STUDENT
                );

                userRepository.save(user);
            }

            // =========================
            // Sample hostel
            // =========================

            if (hostelRepository.count() == 0) {

                Hostel hostel = new Hostel();

                hostel.setName(
                        "Sample Student Hostel"
                );

                hostel.setDescription(
                        "A sample hostel for testing the application."
                );

                hostel.setAddress(
                        "Near Anurag University"
                );

                hostel.setArea(
                        "Ghatkesar"
                );

                hostel.setCity(
                        "Hyderabad"
                );

                hostel.setMonthlyRent(
                        8000.0
                );

                hostel.setContactNumber(
                        "9876543210"
                );

                hostelRepository.save(hostel);
            }
        };
    }
}