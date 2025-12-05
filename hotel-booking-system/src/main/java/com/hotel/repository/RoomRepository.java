package com.hotel.repository;

import com.hotel.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    
    /**
     * Знайти всі номери готелю.
     */
    List<Room> findByHotelId(Long hotelId);
    
    /**
     * Знайти доступні номери готелю.
     */
    List<Room> findByHotelIdAndAvailableTrue(Long hotelId);
    
    /**
     * Знайти номери за типом.
     */
    List<Room> findByType(Room.RoomType type);
}
