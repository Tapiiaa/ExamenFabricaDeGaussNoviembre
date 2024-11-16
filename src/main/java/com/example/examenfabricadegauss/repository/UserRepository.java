package com.example.examenfabricadegauss.repository;

import com.example.examenfabricadegauss.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);

    AppUser findByEmail(String email);

}
