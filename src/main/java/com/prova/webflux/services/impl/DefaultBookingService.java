package com.prova.webflux.services.impl;

import com.prova.webflux.domains.Booking;
import com.prova.webflux.dto.BookingDTO;
import com.prova.webflux.repos.BookingMongoRepository;
import com.prova.webflux.repos.UserMongoRepository;
import com.prova.webflux.services.api.IBookingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DefaultBookingService implements IBookingService {

    private final BookingMongoRepository bookingMongoRepository;
    private final UserMongoRepository userMongoRepository;
    private final ModelMapper modelMapper;

    @Override
    public Flux<BookingDTO> findAll() {
        return bookingMongoRepository.findAll()
                .map(savedBooking -> modelMapper.map(savedBooking, BookingDTO.class));
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
                            .map(savedBooking -> modelMapper.map(savedBooking, BookingDTO.class));

                });
    }

    @Override
    public Mono<BookingDTO> findById(String bookingId) {
        return bookingMongoRepository.findById(bookingId)
                .map(savedBooking -> modelMapper.map(savedBooking, BookingDTO.class));
    }

    @Override
    public Flux<BookingDTO> findByDate(LocalDate bookingDate) {
        return bookingMongoRepository.findBookingsByDate(bookingDate)
                .map(booking -> modelMapper.map(booking, BookingDTO.class));
    }

    @Override
    public Flux<BookingDTO> findByDateRange(LocalDate fromDate, LocalDate toDate) {
        return bookingMongoRepository.findBookingsByDateBetween(fromDate, toDate)
                .map(booking -> modelMapper.map(booking, BookingDTO.class));
    }


    @Override
    public Mono<BookingDTO> update(String id, BookingDTO bookingDTO) {
        return bookingMongoRepository.findById(id)
                .flatMap(existingBooking -> {
                    existingBooking.setDate(bookingDTO.getDate() == null ? existingBooking.getDate() : bookingDTO.getDate());
                    existingBooking.setTime(bookingDTO.getTime() == null ? existingBooking.getTime() : bookingDTO.getTime());
                    return bookingMongoRepository.save(existingBooking);
                })
                .map(savedBooking -> modelMapper.map(savedBooking, BookingDTO.class));


    }

    @Override
    public Mono<Void> delete(String userId) {
        return bookingMongoRepository.deleteById(userId);
    }
}
