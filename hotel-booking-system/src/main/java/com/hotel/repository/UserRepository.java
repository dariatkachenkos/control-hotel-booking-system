package com.hotel.repository;

import com.hotel.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    
    /**
     * Перевірити, чи існує користувач з таким ім'ям.
     */
    Boolean existsByUsername(String username);
    
    /**
     * Перевірити, чи існує користувач з такою поштою.
     */
    Boolean existsByEmail(String email);
}
