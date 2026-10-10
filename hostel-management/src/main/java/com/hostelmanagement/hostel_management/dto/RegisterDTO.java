
package com.hostelmanagement.hostel_management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @Pattern(
    regexp = "(?i)^$|^\\d{2}eg\\d+[a-z]\\d+$",
    message = "Invalid roll number format. Example: 24eg109a38"
    )
    private String rollNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    private String email;

    private String mobile;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must contain at least 8 characters")
    private String password;

    public RegisterDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}