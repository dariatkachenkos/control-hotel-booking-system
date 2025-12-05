package com.hotel.dto;

import com.hotel.entity.Booking;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO для бронювання.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    
    private Long id;
    
    @NotNull(message = "ID номера обов'язковий")
    private Long roomId;
    
    @NotNull(message = "Дата заїзду обов'язкова")
    @FutureOrPresent(message = "Дата заїзду не може бути в минулому")
    private LocalDate checkInDate;
    
    @NotNull(message = "Дата виїзду обов'язкова")
    @Future(message = "Дата виїзду має бути в майбутньому")
    private LocalDate checkOutDate;
    
    // Поля для відповіді
    private BigDecimal totalPrice;
    private Booking.BookingStatus status;
    private String guestName;
    private String roomNumber;
    private String hotelName;
    private Long nights;
}
