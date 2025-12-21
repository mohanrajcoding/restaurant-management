package com.batman.authservice.repository;

import com.batman.authservice.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUserName(String userName);
    Optional<Users> findByEmail(String email);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
}
