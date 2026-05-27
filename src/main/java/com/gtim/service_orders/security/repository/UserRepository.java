package com.gtim.service_orders.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gtim.service_orders.security.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);
    
    Optional<User> findByEmail(String email);
}
