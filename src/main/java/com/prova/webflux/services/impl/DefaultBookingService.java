package com.prova.webflux.services.impl;

import com.prova.webflux.domains.Booking;
import com.prova.webflux.dto.BookingDTO;
import com.prova.webflux.repos.BookingMongoRepository;
import com.prova.webflux.repos.UserMongoRepository;
import com.prova.webflux.services.api.IBookingService;
import com.prova.webflux.services.api.IUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DefaultBookingService implements IBookingService {

    private final BookingMongoRepository bookingMongoRepository;
    private final UserMongoRepository userMongoRepository;

    @Override
    public Flux<BookingDTO> findAll() {
        return bookingMongoRepository.findAll()
                .map(savedBooking -> {
                    BookingDTO bookingDTO = new BookingDTO();
                    bookingDTO.setId(savedBooking.getId());
                    bookingDTO.setTime(savedBooking.getTime());
                    bookingDTO.setDate(savedBooking.getDate());
                    bookingDTO.setUserId(savedBooking.getUser().getId());
                    return bookingDTO;
                });
    }

    @Override
    public Mono<BookingDTO> save(BookingDTO bookingDTO) {
        return userMongoRepository.findById(bookingDTO.getUserId())
                .flatMap(user -> {
                    Booking booking = new Booking();
                    booking.setDate(bookingDTO.getDate());
                    booking.setTime(bookingDTO.getTime());
                    booking.setUser(user);
                    return bookingMongoRepository.save(booking)
                            .map(savedBooking -> {
                                BookingDTO result = new BookingDTO();
                                result.setId(savedBooking.getId());
                                result.setTime(savedBooking.getTime());
                                result.setDate(savedBooking.getDate());
                                result.setUserId(savedBooking.getUser().getId());
                                return result;
                            });
                });
    }

    @Override
    public Mono<BookingDTO> findById(String bookingId) {
        return bookingMongoRepository.findById(bookingId)
                .map(savedBooking -> {
                    BookingDTO bookingDTO = new BookingDTO();
                    bookingDTO.setId(savedBooking.getId());
                    bookingDTO.setTime(savedBooking.getTime());
                    bookingDTO.setDate(savedBooking.getDate());
                    bookingDTO.setUserId(savedBooking.getUser().getId());
                    return bookingDTO;
                });
    }


    @Override
    public Mono<BookingDTO> update(String id, BookingDTO bookingDTO) {
        return bookingMongoRepository.findById(id)
                .flatMap(existingBooking -> {
                    existingBooking.setDate(bookingDTO.getDate());
                    existingBooking.setTime(bookingDTO.getTime());
                    return userMongoRepository.findById(bookingDTO.getUserId())
                            .flatMap(user -> {
                                existingBooking.setUser(user);
                                return bookingMongoRepository.save(existingBooking)
                                        .map(savedBooking -> {
                                            BookingDTO result = new BookingDTO();
                                            result.setId(id);
                                            result.setTime(savedBooking.getTime());
                                            result.setDate(savedBooking.getDate());
                                            result.setUserId(savedBooking.getUser().getId());
                                            return result;
                                        });
                            });
                });
    }

    @Override
    public Mono<Void> delete(String userId) {
        return bookingMongoRepository.deleteById(userId);
    }
}
