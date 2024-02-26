package com.prova.webflux.services.api;

import com.prova.webflux.dto.BookingDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface IBookingService {

    Flux<BookingDTO> findAll();

    Mono<BookingDTO> save(BookingDTO bookingDTO);

    Mono<BookingDTO> findById(String userId);

    Flux<BookingDTO> findByDate(LocalDate bookingDate);

    Flux<BookingDTO> findByDateRange(LocalDate fromDate, LocalDate toDate);

    Mono<BookingDTO> update(String id, BookingDTO userDTO);

    Mono<Void> delete(String userId);
}
