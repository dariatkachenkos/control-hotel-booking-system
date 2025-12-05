package com.hotel.controller;

import com.hotel.dto.BookingDto;
import com.hotel.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контролер для роботи з бронюваннями.
 */
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    
    private final BookingService bookingService;
    
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }
    
    /**
     * Створити нове бронювання (для авторизованих користувачів).
     */
    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@Valid @RequestBody BookingDto bookingDto) {
        BookingDto created = bookingService.createBooking(bookingDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    
    /**
     * Отриматибронювання.
     */
    @GetMapping("/my")
    public ResponseEntity<List<BookingDto>> getMyBookings() {
        List<BookingDto> bookings = bookingService.getMyBookings();
        return ResponseEntity.ok(bookings);
    }
    
    /**
     * Отримати бронювання за ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Long id) {
        BookingDto booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(booking);
    }
    
    /**
     * Отримати всі бронювання готелю.
     */
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<BookingDto>> getBookingsByHotel(@PathVariable Long hotelId) {
        List<BookingDto> bookings = bookingService.getBookingsByHotel(hotelId);
        return ResponseEntity.ok(bookings);
    }
    
    /**
     * Отримати всі бронювання (тільки адмін).
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingDto>> getAllBookings() {
        List<BookingDto> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }
    
    /**
     * Скасувати бронювання (тільки адмін).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<BookingDto> cancelBooking(@PathVariable Long id) {
        BookingDto cancelled = bookingService.cancelBooking(id);
        return ResponseEntity.ok(cancelled);
    }
}
