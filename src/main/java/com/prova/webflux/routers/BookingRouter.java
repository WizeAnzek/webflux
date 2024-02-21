package com.prova.webflux.routers;
import com.prova.webflux.handlers.BookingHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BookingRouter {

    private BookingHandler bookingHandler;

    public BookingRouter(BookingHandler bookingHandler) {
        this.bookingHandler = bookingHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> bookingsRoutes() {
        return route()
                .path("/bookings", builder -> builder
                        .GET("", bookingHandler::findAll)
                        .POST("", accept(MediaType.APPLICATION_JSON), bookingHandler::save)
                )
                .build();
    }
}