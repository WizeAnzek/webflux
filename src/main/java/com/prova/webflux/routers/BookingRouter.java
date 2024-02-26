package com.prova.webflux.routers;

import com.prova.webflux.handlers.BookingHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BookingRouter {
    private final BookingHandler bookingHandler;

    public BookingRouter(BookingHandler bookingHandler) {
        this.bookingHandler = bookingHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> bookingsRoutes() {
        return route()
                .path("/bookings", builder -> builder
                        .GET("", bookingHandler::findAll)
                        .GET("/{id}", bookingHandler::findById)
                        .GET("/searchByDate/", bookingHandler::findByDate)
                        .PUT("/{id}", accept(MediaType.APPLICATION_JSON), bookingHandler::update)
                        .POST("", accept(MediaType.APPLICATION_JSON), bookingHandler::save)
                        .DELETE("/{id}", bookingHandler::delete)
                )
                .build();
    }

}