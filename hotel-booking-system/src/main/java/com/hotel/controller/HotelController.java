package com.hotel.controller;

import com.hotel.dto.HotelDto;
import com.hotel.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контролер для роботи з готелями.
 * 
 * GET-запити публічні, POST/PUT/DELETE - тільки для адміна.
 */
@RestController
@RequestMapping("/api/hotels")
public class HotelController {
    
    private final HotelService hotelService;
    
    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }
    
    /**
     * Отримати всі готелі.
     * GET /api/hotels
     */
    @GetMapping
    public ResponseEntity<List<HotelDto>> getAllHotels() {
        List<HotelDto> hotels = hotelService.getAllHotels();
        return ResponseEntity.ok(hotels);
    }
    
    /**
     * Отримати готель за ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HotelDto> getHotelById(@PathVariable Long id) {
        HotelDto hotel = hotelService.getHotelById(id);
        return ResponseEntity.ok(hotel);
    }
    
    /**
     * Пошук готелів за містом.
     */
    @GetMapping("/search")
    public ResponseEntity<List<HotelDto>> searchByCity(@RequestParam String city) {
        List<HotelDto> hotels = hotelService.getHotelsByCity(city);
        return ResponseEntity.ok(hotels);
    }
    
    /**
     * Створити новий готель (тільки адмін).
     */
    @PostMapping
    public ResponseEntity<HotelDto> createHotel(@Valid @RequestBody HotelDto hotelDto) {
        HotelDto created = hotelService.createHotel(hotelDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    
    /**
     * Оновити готель (тільки адмін).
     */
    @PutMapping("/{id}")
    public ResponseEntity<HotelDto> updateHotel(
            @PathVariable Long id,
            @Valid @RequestBody HotelDto hotelDto) {
        HotelDto updated = hotelService.updateHotel(id, hotelDto);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * Видалити готель (тільки адмін).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.noContent().build();
    }
}
