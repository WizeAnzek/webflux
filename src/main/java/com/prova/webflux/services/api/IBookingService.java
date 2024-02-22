package com.prova.webflux.services.api;

import com.prova.webflux.domains.Booking;
import com.prova.webflux.dto.BookingDTO;
import com.prova.webflux.dto.UserDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBookingService {

    Flux<BookingDTO> findAll();
    Mono<BookingDTO> save(BookingDTO bookingDTO);
    Mono<BookingDTO> findById(String userId);
    Mono<BookingDTO> update(String id, BookingDTO userDTO);
    Mono<Void> delete(String userId);
}
