package com.hotel.service;

import com.hotel.dto.HotelDto;
import com.hotel.entity.Hotel;
import com.hotel.exception.ResourceNotFoundException;
import com.hotel.repository.HotelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class  HotelService {
    
    private static final Logger logger = LoggerFactory.getLogger(HotelService.class);
    
    private final HotelRepository hotelRepository;
    
    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }
    
    /**
     * Отримати всі готелі.
     */
    @Transactional(readOnly = true)
    public List<HotelDto> getAllHotels() {
        logger.info("Отримання списку всіх готелів");
        return hotelRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Отримати готель за ID.
     */
    @Transactional(readOnly = true)
    public HotelDto getHotelById(Long id) {
        logger.info("Пошук готелю з ID: {}", id);
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Готель", "id", id));
        return convertToDto(hotel);
    }
    
    /**
     * Створити новий готель.
     */
    public HotelDto createHotel(HotelDto hotelDto) {
        logger.info("Створення нового готелю: {}", hotelDto.getName());
        
        Hotel hotel = new Hotel();
        hotel.setName(hotelDto.getName());
        hotel.setAddress(hotelDto.getAddress());
        hotel.setCity(hotelDto.getCity());
        hotel.setStars(hotelDto.getStars());
        hotel.setDescription(hotelDto.getDescription());
        
        hotel = hotelRepository.save(hotel);
        
        logger.info("Готель {} успішно створено з ID: {}", hotel.getName(), hotel.getId());
        return convertToDto(hotel);
    }
    
    /**
     * Оновити готель.
     */
    public HotelDto updateHotel(Long id, HotelDto hotelDto) {
        logger.info("Оновлення готелю з ID: {}", id);
        
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Готель", "id", id));
        
        hotel.setName(hotelDto.getName());
        hotel.setAddress(hotelDto.getAddress());
        hotel.setCity(hotelDto.getCity());
        hotel.setStars(hotelDto.getStars());
        hotel.setDescription(hotelDto.getDescription());
        
        hotel = hotelRepository.save(hotel);
        
        logger.info("Готель {} успішно оновлено", hotel.getName());
        return convertToDto(hotel);
    }
    
    /**
     * Видалити готель.
     */
    public void deleteHotel(Long id) {
        logger.info("Видалення готелю з ID: {}", id);
        
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Готель", "id", id));
        
        hotelRepository.delete(hotel);
        logger.info("Готель {} успішно видалено", hotel.getName());
    }
    
    /**
     * Пошук готелів за містом.
     */
    @Transactional(readOnly = true)
    public List<HotelDto> getHotelsByCity(String city) {
        logger.info("Пошук готелів у місті: {}", city);
        return hotelRepository.findByCity(city)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Конвертація Entity в DTO.
     */
    private HotelDto convertToDto(Hotel hotel) {
        HotelDto dto = new HotelDto();
        dto.setId(hotel.getId());
        dto.setName(hotel.getName());
        dto.setAddress(hotel.getAddress());
        dto.setCity(hotel.getCity());
        dto.setStars(hotel.getStars());
        dto.setDescription(hotel.getDescription());
        dto.setRoomCount(hotel.getRooms() != null ? hotel.getRooms().size() : 0);
        return dto;
    }
}
