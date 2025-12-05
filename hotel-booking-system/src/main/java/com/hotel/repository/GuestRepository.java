package com.hotel.repository;

import com.hotel.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
    
    /**
     * Знайти гостя за ID користувача.
     */
    Optional<Guest> findByUserId(Long userId);
    
    /**
     * Знайти гостя за номером телефону.
     */
    Optional<Guest> findByPhone(String phone);
}
