package com.hotel.controller;

import com.hotel.dto.RoomDto;
import com.hotel.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контролер для роботи з номерами.
 */
@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    
    private final RoomService roomService;
    
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }
    
    /**
     * Отримати всі номери готелю.
     */
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<RoomDto>> getRoomsByHotel(@PathVariable Long hotelId) {
        List<RoomDto> rooms = roomService.getRoomsByHotel(hotelId);
        return ResponseEntity.ok(rooms);
    }
    
    /**
     * Отримати доступні номери готелю.
     */
    @GetMapping("/hotel/{hotelId}/available")
    public ResponseEntity<List<RoomDto>> getAvailableRooms(@PathVariable Long hotelId) {
        List<RoomDto> rooms = roomService.getAvailableRooms(hotelId);
        return ResponseEntity.ok(rooms);
    }
    
    /**
     * Отримати номер за ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long id) {
        RoomDto room = roomService.getRoomById(id);
        return ResponseEntity.ok(room);
    }
    
    /**
     * Створити номер у готелі (тільки адмін).
     */
    @PostMapping("/hotel/{hotelId}")
    public ResponseEntity<RoomDto> createRoom(
            @PathVariable Long hotelId,
            @Valid @RequestBody RoomDto roomDto) {
        RoomDto created = roomService.createRoom(hotelId, roomDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    
    /**
     * Оновити номер (тільки адмін).
     */
    @PutMapping("/{id}")
    public ResponseEntity<RoomDto> updateRoom(
            @PathVariable Long id,
            @Valid @RequestBody RoomDto roomDto) {
        RoomDto updated = roomService.updateRoom(id, roomDto);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * Видалити номер (тільки адмін).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}
