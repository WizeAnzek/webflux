package com.prova.webflux.services.api;

import com.prova.webflux.domains.Booking;
import com.prova.webflux.dto.BookingDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBookingService {

    Flux<BookingDTO> findAll();
    Mono<BookingDTO> save(BookingDTO bookingDTO);
}
