package com.prova.webflux.services.impl;

import com.prova.webflux.domains.User;
import com.prova.webflux.dto.UserDTO;
import com.prova.webflux.repos.UserMongoRepository;
import com.prova.webflux.services.api.IUserService;
import com.prova.webflux.utils.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements IUserService {

    private final UserMongoRepository userMongoRepository;
    @Override
    public Flux<UserDTO> findAll() {
        return userMongoRepository.findAll()
                .map(Converter::entityToDto);

    }

    @Override
    public Mono<UserDTO> save(UserDTO userDTO) {
        User user = new User(userDTO.getName(), userDTO.getEmail());
        return userMongoRepository.save(user)
                .map(Converter::entityToDto);
    }

    public Mono<User> findById(String id) {
        return userMongoRepository.findById(id);
    }
}
