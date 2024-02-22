package com.prova.webflux.handlers;

import com.prova.webflux.dto.BookingDTO;
import com.prova.webflux.dto.request.CreateBookingRequestDTO;
import com.prova.webflux.dto.response.GetBookingsResponseDTO;
import com.prova.webflux.services.api.IBookingService;
import com.prova.webflux.dto.response.GetBookingsResponseDTO.GetBookingResponseDTO;
import com.prova.webflux.services.api.IUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class BookingHandler {


    private static final Logger LOGGER = LoggerFactory.getLogger(BookingHandler.class);

    private final IBookingService bookingService;
    private final IUserService userService;

    private final ModelMapper modelMapper;


    public Mono<ServerResponse> findAll(ServerRequest request) {
        return bookingService.findAll()
                .collectList()
                .flatMap(bookings -> {
                    if (bookings.isEmpty()) {
                        return ServerResponse.noContent().build();
                    }
                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromValue(bookings));
                });
    }

    public Mono<ServerResponse> save(ServerRequest request) {
       return request.bodyToMono(CreateBookingRequestDTO.class)
                .map(createBookingRequestDTO -> modelMapper.map(createBookingRequestDTO, BookingDTO.class))
                .flatMap(bookingService::save)
                .doOnSuccess(bookingSaved -> LOGGER.info("Booking saved with id: " + bookingSaved.getId()))
                .doOnError(e -> LOGGER.error("Error in saveBooking method", e))
                .map(bookingSaved -> UriComponentsBuilder.fromPath(("/{id}")).buildAndExpand(bookingSaved.getId()).toUri())
                .flatMap(uri -> ServerResponse.created(uri).build());
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        String bookingId = request.pathVariable("id");
        return bookingService.findById(bookingId)
                .flatMap(bookingDTO -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(bookingDTO)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        String bookingId = request.pathVariable("id");
        return request.bodyToMono(BookingDTO.class)
                .flatMap(bookingDTO -> {
                    bookingDTO.setId(bookingId);
                    return bookingService.update(bookingId, bookingDTO);
                })
                .flatMap(updatedBooking -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(updatedBooking)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String bookingId = request.pathVariable("id");
        return bookingService.delete(bookingId)
                .then(ServerResponse.ok().build())
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}