
package com.hostelmanagement.hostel_management.service;

import com.hostelmanagement.hostel_management.dto.LoginDTO;
import com.hostelmanagement.hostel_management.dto.RegisterDTO;
import com.hostelmanagement.hostel_management.entity.User;
import com.hostelmanagement.hostel_management.enums.Role;
import com.hostelmanagement.hostel_management.repository.UserRepository;
import com.hostelmanagement.hostel_management.security.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String register(RegisterDTO dto) {

        String email = dto.getEmail()
                .trim()
                .toLowerCase(Locale.ROOT);

        String rollNumber = dto.getRollNumber() == null
                ? null
                : dto.getRollNumber()
                        .trim()
                        .toLowerCase(Locale.ROOT);

        String mobile = dto.getMobile() == null
                || dto.getMobile().isBlank()
                ? null
                : dto.getMobile().trim();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        if (rollNumber != null && !rollNumber.isBlank()) {

            if (userRepository.findByRollNumber(rollNumber).isPresent()) {
                throw new RuntimeException(
                        "Roll number already registered");
            }

        } else {
            rollNumber = null;
        }

        if (mobile != null
                && userRepository.findByMobile(mobile).isPresent()) {

            throw new RuntimeException(
                    "Mobile number already registered");
        }

        Role assignedRole = rollNumber == null
                ? Role.NEW_STUDENT
                : determineRole(rollNumber);

        User user = new User();

        user.setName(dto.getName().trim());
        user.setRollNumber(rollNumber);
        user.setEmail(email);
        user.setMobile(mobile);
        user.setPassword(
                passwordEncoder.encode(dto.getPassword()));
        user.setRole(assignedRole);

        userRepository.save(user);

        return "Registration successful. Assigned role: "
                + assignedRole.name();
    }

    private Role determineRole(String rollNumber) {

        if (!rollNumber.matches("(?i)^\\d{2}eg\\d+[a-z]\\d+$")) {
            throw new RuntimeException(
                    "Invalid roll number format");
        }

        int admissionYear =
                2000 + Integer.parseInt(
                        rollNumber.substring(0, 2));

        int currentYear = Year.now().getValue();

        if (admissionYear > currentYear) {
            throw new RuntimeException(
                    "Future admission-year roll numbers are not allowed");
        }

        if (admissionYear == currentYear) {
            return Role.NEW_STUDENT;
        }

        return Role.VERIFIED_OLD_STUDENT;
    }

    public Map<String, Object> login(LoginDTO dto) {

        String email = dto.getEmail()
                .trim()
                .toLowerCase(Locale.ROOT);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid email or password"));

        if (!passwordEncoder.matches(
                dto.getPassword(),
                user.getPassword())) {

            throw new RuntimeException(
                    "Invalid email or password");
        }

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("token", token);
        response.put("userId", user.getId());
        response.put("role", user.getRole().name());

        return response;
    }
}