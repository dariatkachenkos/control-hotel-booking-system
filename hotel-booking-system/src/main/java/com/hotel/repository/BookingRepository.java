package com.hotel.repository;

import com.hotel.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    /**
     * Знайти бронювання за ID гостя.
     */
    List<Booking> findByGuestId(Long guestId);
    
    /**
     * Знайти бронювання за ID номера.
     */
    List<Booking> findByRoomId(Long roomId);
    
    /**
     * Знайти всі бронювання конкретного готелю.
     * Використовуємо JPQL (Java Persistence Query Language) запит.
     */
    @Query("SELECT b FROM Booking b WHERE b.room.hotel.id = :hotelId")
    List<Booking> findByHotelId(@Param("hotelId") Long hotelId);
    
    /**
     * Знайти бронювання за статусом.
     */
    List<Booking> findByStatus(Booking.BookingStatus status);
    
    /**
     * Перевірити, чи є конфліктуючі бронювання для номера на вказані дати.
     * Бронювання конфліктує, якщо періоди перетинаються і статус не CANCELLED.
     */
    @Query("SELECT b FROM Booking b WHERE b.room.id = :roomId " +
           "AND b.status != 'CANCELLED' " +
            "AND ((b.checkInDate < :checkOut AND b.checkOutDate > :checkIn))")
    List<Booking> findConflictingBookings(
            @Param("roomId") Long roomId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );
}
