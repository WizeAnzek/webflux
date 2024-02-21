package com.prova.webflux.services.api;

import com.prova.webflux.domains.User;
import com.prova.webflux.dto.UserDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUserService {

    Flux<UserDTO> findAll();
    Mono<UserDTO> save(UserDTO userDTO);

    Mono<User> findById(String userId);
}
