package com.prova.webflux.routers;

import com.prova.webflux.handlers.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRouter {

    private UserHandler userHandler;

    public UserRouter(UserHandler userHandler) {
        this.userHandler = userHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> userRoutes() {
        return route()
                .path("/users", builder -> builder
                        .GET("", userHandler::findAll)
                        .GET("/{id}", userHandler::findById)
                        .PUT("/{id}", accept(MediaType.APPLICATION_JSON), userHandler::update)
                        .POST("", accept(MediaType.APPLICATION_JSON), userHandler::save)
                        .DELETE("/{id}", userHandler::delete)
                )
                .build();
    }
}