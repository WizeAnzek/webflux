package com.prova.webflux.handlers;

import com.prova.webflux.dto.BookingDTO;
import com.prova.webflux.dto.UserDTO;
import com.prova.webflux.dto.request.CreateUserRequestDTO;
import com.prova.webflux.dto.response.GetUsersResponseDTO;
import com.prova.webflux.services.api.IUserService;
import com.prova.webflux.dto.response.GetUsersResponseDTO.GetUserResponseDTO;
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
public class UserHandler {


    private static final Logger LOGGER = LoggerFactory.getLogger(UserHandler.class);

    private final IUserService userService;

    private final ModelMapper modelMapper;


    public Mono<ServerResponse> findAll(ServerRequest request) {
        return userService.findAll()
                .collectList()
                .flatMap(users -> {
                    if (users.isEmpty()) {
                        return ServerResponse.noContent().build();
                    }
                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromValue(users));
                });
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        String bookingId = request.pathVariable("id");
        return userService.findById(bookingId)
                .flatMap(bookingDTO -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(bookingDTO)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        return request.bodyToMono(CreateUserRequestDTO.class)
                .map(createUserRequestDTO -> modelMapper.map(createUserRequestDTO, UserDTO.class))
                .flatMap(userService::save)
                .doOnSuccess(userSaved -> LOGGER.info("User saved with id: " + userSaved.getId()))
                .doOnError(e -> LOGGER.error("Error in saveUser method", e))
                .map(userSaved -> UriComponentsBuilder.fromPath(("/{id}")).buildAndExpand(userSaved.getId()).toUri())
                .flatMap(uri -> ServerResponse.created(uri).build());
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String userId = request.pathVariable("id");
        return userService.delete(userId)
                .then(ServerResponse.ok().build())
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        String userId = request.pathVariable("id");
        return request.bodyToMono(UserDTO.class)
                .flatMap(userDTO -> userService.update(userId, userDTO))
                .flatMap(updatedUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(updatedUser)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}