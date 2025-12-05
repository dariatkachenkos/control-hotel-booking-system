package com.hotel.service;

import com.hotel.dto.BookingDto;
import com.hotel.entity.Booking;
import com.hotel.entity.Guest;
import com.hotel.entity.Room;
import com.hotel.entity.User;
import com.hotel.exception.BadRequestException;
import com.hotel.exception.ResourceNotFoundException;
import com.hotel.repository.BookingRepository;
import com.hotel.repository.GuestRepository;
import com.hotel.repository.RoomRepository;
import com.hotel.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class BookingService {
    
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);
    
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    private final UserRepository userRepository;
    
    public BookingService(BookingRepository bookingRepository,
                         RoomRepository roomRepository,
                         GuestRepository guestRepository,
                         UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
        this.userRepository = userRepository;
    }
    
    /**
     * Створити нове бронювання.
     */
    public BookingDto createBooking(BookingDto bookingDto) {
        // Отримуємо поточного користувача
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Створення бронювання користувачем: {}", username);
        
        // Знаходимо користувача та його профіль гостя
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Користувач", "username", username));
        
        Guest guest = guestRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BadRequestException("Профіль гостя не знайдено"));
        
        // Знаходимо номер
        Room room = roomRepository.findById(bookingDto.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Номер", "id", bookingDto.getRoomId()));
        
        // Перевіряємо доступність номера
        if (!room.getAvailable()) {
            throw new BadRequestException("Номер недоступний для бронювання");
        }
        
        // Перевіряємо дати
        if (bookingDto.getCheckOutDate().isBefore(bookingDto.getCheckInDate()) ||
            bookingDto.getCheckOutDate().isEqual(bookingDto.getCheckInDate())) {
            throw new BadRequestException("Дата виїзду має бути пізніше дати заїзду");
        }
        
        // Перевіряємо конфлікти з іншими бронюваннями
        List<Booking> conflicts = bookingRepository.findConflictingBookings(
                room.getId(),
                bookingDto.getCheckInDate(),
                bookingDto.getCheckOutDate()
        );
        
        if (!conflicts.isEmpty()) {
            throw new BadRequestException("Номер вже заброньований на вказані дати");
        }
        
        // Створюємо бронювання
        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setGuest(guest);
        booking.setCheckInDate(bookingDto.getCheckInDate());
        booking.setCheckOutDate(bookingDto.getCheckOutDate());
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        
        // Розраховуємо вартість
        long nights = booking.getNights();
        BigDecimal totalPrice = room.getPricePerNight().multiply(BigDecimal.valueOf(nights));
        booking.setTotalPrice(totalPrice);
        
        booking = bookingRepository.save(booking);
        
        logger.info("Бронювання ID: {} успішно створено. Вартість: {}", 
                booking.getId(), totalPrice);
        
        return convertToDto(booking);
    }
    
    /**
     * Отримати бронювання за ID.
     */
    @Transactional(readOnly = true)
    public BookingDto getBookingById(Long id) {
        logger.info("Пошук бронювання з ID: {}", id);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Бронювання", "id", id));
        return convertToDto(booking);
    }
    
    /**
     * Отримати всі бронювання поточного користувача.
     */
    @Transactional(readOnly = true)
    public List<BookingDto> getMyBookings() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Отримання бронювань користувача: {}", username);
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Користувач", "username", username));
        
        Guest guest = guestRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BadRequestException("Профіль гостя не знайдено"));
        
        return bookingRepository.findByGuestId(guest.getId())
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Отримати всі бронювання готелю.
     */
    @Transactional(readOnly = true)
    public List<BookingDto> getBookingsByHotel(Long hotelId) {
        logger.info("Отримання бронювань готелю ID: {}", hotelId);
        return bookingRepository.findByHotelId(hotelId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Отримати всі бронювання (тільки для адміна).
     */
    @Transactional(readOnly = true)
    public List<BookingDto> getAllBookings() {
        logger.info("Отримання всіх бронювань");
        return bookingRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Скасувати бронювання (тільки для адміна).
     */
    public BookingDto cancelBooking(Long id) {
        logger.info("Скасування бронювання ID: {}", id);
        
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Бронювання", "id", id));
        
        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new BadRequestException("Бронювання вже скасовано");
        }
        
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        booking = bookingRepository.save(booking);
        
        logger.info("Бронювання ID: {} успішно скасовано", id);
        return convertToDto(booking);
    }
    
    /**
     * Конвертація Entity в DTO.
     */
    private BookingDto convertToDto(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setRoomId(booking.getRoom().getId());
        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());
        dto.setTotalPrice(booking.getTotalPrice());
        dto.setStatus(booking.getStatus());
        dto.setGuestName(booking.getGuest().getFullName());
        dto.setRoomNumber(booking.getRoom().getRoomNumber());
        dto.setHotelName(booking.getRoom().getHotel().getName());
        dto.setNights(booking.getNights());
        return dto;
    }
}
