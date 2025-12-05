package com.hotel.service;

import com.hotel.dto.RoomDto;
import com.hotel.entity.Hotel;
import com.hotel.entity.Room;
import com.hotel.exception.ResourceNotFoundException;
import com.hotel.repository.HotelRepository;
import com.hotel.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoomService {
    
    private static final Logger logger = LoggerFactory.getLogger(RoomService.class);
    
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    
    public RoomService(RoomRepository roomRepository, HotelRepository hotelRepository) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
    }
    
    /**
     * Отримати всі номери готелю.
     */
    @Transactional(readOnly = true)
    public List<RoomDto> getRoomsByHotel(Long hotelId) {
        logger.info("Отримання номерів готелю ID: {}", hotelId);
        
        // Перевіряємо чи існує готель
        if (!hotelRepository.existsById(hotelId)) {
            throw new ResourceNotFoundException("Готель", "id", hotelId);
        }
        
        return roomRepository.findByHotelId(hotelId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Отримати доступні номери готелю.
     */
    @Transactional(readOnly = true)
    public List<RoomDto> getAvailableRooms(Long hotelId) {
        logger.info("Отримання доступних номерів готелю ID: {}", hotelId);
        
        return roomRepository.findByHotelIdAndAvailableTrue(hotelId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Отримати номер за ID.
     */
    @Transactional(readOnly = true)
    public RoomDto getRoomById(Long id) {
        logger.info("Пошук номера з ID: {}", id);
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Номер", "id", id));
        return convertToDto(room);
    }
    
    /**
     * Створити новий номер.
     */
    public RoomDto createRoom(Long hotelId, RoomDto roomDto) {
        logger.info("Створення номера {} в готелі ID: {}", roomDto.getRoomNumber(), hotelId);
        
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Готель", "id", hotelId));
        
        Room room = new Room();
        room.setRoomNumber(roomDto.getRoomNumber());
        room.setType(roomDto.getType());
        room.setPricePerNight(roomDto.getPricePerNight());
        room.setCapacity(roomDto.getCapacity());
        room.setDescription(roomDto.getDescription());
        room.setAvailable(roomDto.getAvailable() != null ? roomDto.getAvailable() : true);
        room.setHotel(hotel);
        
        room = roomRepository.save(room);
        
        logger.info("Номер {} успішно створено з ID: {}", room.getRoomNumber(), room.getId());
        return convertToDto(room);
    }
    
    /**
     * Оновити номер.
     */
    public RoomDto updateRoom(Long id, RoomDto roomDto) {
        logger.info("Оновлення номера з ID: {}", id);
        
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Номер", "id", id));
        
        room.setRoomNumber(roomDto.getRoomNumber());
        room.setType(roomDto.getType());
        room.setPricePerNight(roomDto.getPricePerNight());
        room.setCapacity(roomDto.getCapacity());
        room.setDescription(roomDto.getDescription());
        if (roomDto.getAvailable() != null) {
            room.setAvailable(roomDto.getAvailable());
        }
        
        room = roomRepository.save(room);
        
        logger.info("Номер {} успішно оновлено", room.getRoomNumber());
        return convertToDto(room);
    }
    
    /**
     * Видалити номер.
     */
    public void deleteRoom(Long id) {
        logger.info("Видалення номера з ID: {}", id);
        
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Номер", "id", id));
        
        roomRepository.delete(room);
        logger.info("Номер {} успішно видалено", room.getRoomNumber());
    }
    
    /**
     * Конвертація Entity в DTO.
     */
    private RoomDto convertToDto(Room room) {
        RoomDto dto = new RoomDto();
        dto.setId(room.getId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setType(room.getType());
        dto.setPricePerNight(room.getPricePerNight());
        dto.setCapacity(room.getCapacity());
        dto.setDescription(room.getDescription());
        dto.setAvailable(room.getAvailable());
        dto.setHotelId(room.getHotel().getId());
        dto.setHotelName(room.getHotel().getName());
        return dto;
    }
}
