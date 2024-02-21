package com.prova.webflux.services.impl;

import com.prova.webflux.domains.Booking;
import com.prova.webflux.domains.User;
import com.prova.webflux.dto.BookingDTO;
import com.prova.webflux.repos.BookingMongoRepository;
import com.prova.webflux.services.api.IBookingService;
import com.prova.webflux.services.api.IUserService;
import com.prova.webflux.utils.Converter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DefaultBookingService implements IBookingService {
    private final BookingMongoRepository bookingMongoRepository;
    private final ModelMapper modelMapper;
    private final IUserService userService;
    @Override
    public Flux<BookingDTO> findAll() {
        return bookingMongoRepository.findAll()
                .map(savedBooking -> new BookingDTO(savedBooking.getId(), savedBooking.getTime(), savedBooking.getDate(), savedBooking.getUser().getId()));
    }

    @Override
    public Mono<BookingDTO> save(BookingDTO bookingDTO) {
        return userService.findById(bookingDTO.getUserId())
                .flatMap(user -> {
                    Booking booking = new Booking(bookingDTO.getDate(), bookingDTO.getTime(), user);
                    return bookingMongoRepository.save(booking)
                            .map(savedBooking -> new BookingDTO(savedBooking.getId(), savedBooking.getTime(), savedBooking.getDate(), savedBooking.getUser().getId()));
                });
    }
}
