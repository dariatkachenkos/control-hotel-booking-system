package com.hotel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Сутність Бронювання.
 * Зв'язує гостяиз номером (Room) на певний період.
 */
@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Дата заїзду.
     */
    @Column(nullable = false)
    private LocalDate checkInDate;
    
    /**
     * Дата виїзду.
     */
    @Column(nullable = false)
    private LocalDate checkOutDate;
    
    /**
     * Загальна вартість бронювання.
     */
    @Column(nullable = false)
    private BigDecimal totalPrice;
    
    /**
     * Статус бронювання.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;
    
    /**
     * Гість, який зробив бронювання.
     * ManyToOne - багато бронювань можуть належати одному гостю
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id", nullable = false)
    private Guest guest;
    
    /**
     * Заброньований номер.
     * ManyToOne - багато бронювань можуть бути на один номер (у різний час)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;
    
    /**
     * Кількість ночей перебування.
     */
    public long getNights() {
        return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }
    
    /**
     * Статуси бронювання.
     */
    public enum BookingStatus {
        PENDING,    // Очікує підтвердження
        CONFIRMED,  // Підтверджено
        CANCELLED,  // Скасовано
        COMPLETED   // Завершено
    }
}
