package com.prova.webflux.handlers;

import com.prova.webflux.dto.BookingDTO;
import com.prova.webflux.services.api.IBookingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class BookingHandler {


    private static final Logger LOGGER = LoggerFactory.getLogger(BookingHandler.class);

    private final IBookingService bookingService;
    private final CustomWebSocketHandler webSocketHandler;



    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(bookingService.findAll(), BookingDTO.class);
    }

    public Mono<ServerResponse> findByDate(ServerRequest request) {
        Optional<String> firstParam = request.queryParam("date");
        Optional<String> secondParam = request.queryParam("toDate");
        if (firstParam.isEmpty()) {
            return ServerResponse.badRequest().build();
        }
        LocalDate date = LocalDate.parse(firstParam.get());
        if (secondParam.isEmpty()) {
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(bookingService.findByDate(date), BookingDTO.class);
        }
        LocalDate toDate = LocalDate.parse(secondParam.get());
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(bookingService.findByDateRange(date, toDate), BookingDTO.class);
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        return request.bodyToMono(BookingDTO.class)
                .flatMap(bookingService::save)
                .doOnSuccess(bookingSaved -> {
                    LOGGER.info(String.format("Booking saved with id: %s", bookingSaved.getId()));
                    webSocketHandler.sendMessage("Nuovo booking creato con id: " + bookingSaved.getId());
                })
                .doOnError(e -> LOGGER.error("Error in saveBooking method", e))
                .flatMap(bookingSaved -> ServerResponse.created(
                                UriComponentsBuilder.fromPath("/{id}")
                                        .buildAndExpand(bookingSaved.getId())
                                        .toUri())
                        .body(Mono.just(bookingSaved), BookingDTO.class));
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
                .flatMap(bookingDTO -> bookingService.update(bookingId, bookingDTO))
                .flatMap(updatedBooking -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(updatedBooking)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String bookingId = request.pathVariable("id");
        return bookingService.delete(bookingId)
                .doOnSuccess(ignored -> webSocketHandler.sendMessage("Booking con id: " + bookingId + " cancellato con successo"))
                .then(ServerResponse.ok().build())
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}