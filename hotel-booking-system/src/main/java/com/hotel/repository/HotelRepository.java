package com.hotel.repository;

import com.hotel.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    
    /**
     * Знайти готелі за містом.
     */
    List<Hotel> findByCity(String city);
    
    /**
     * Знайти готелі за кількістю зірок.
     */
    List<Hotel> findByStarsGreaterThanEqual(Integer stars);
    
    /**
     * Знайти готелі за назвою (частковий збіг, ігнорування регістру).
     */
    List<Hotel> findByNameContainingIgnoreCase(String name);
}
