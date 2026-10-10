
package com.hostelmanagement.hostel_management.repository;

import com.hostelmanagement.hostel_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByRollNumber(String rollNumber);

    Optional<User> findByMobile(String mobile);
}