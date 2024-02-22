package com.prova.webflux.services.impl;

import com.prova.webflux.domains.User;
import com.prova.webflux.dto.UserDTO;
import com.prova.webflux.repos.UserMongoRepository;
import com.prova.webflux.services.api.IUserService;
import com.prova.webflux.utils.Converter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements IUserService {

    private final UserMongoRepository userMongoRepository;
    private final ModelMapper modelMapper;
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

    public Mono<UserDTO> findById(String userId) {
        return userMongoRepository.findById(userId)
                .map(Converter::entityToDto);
    }

    @Override
    public Mono<UserDTO> update(String userId, UserDTO userDTO) {
        return userMongoRepository.findById(userId)
                .flatMap(existingUser -> {
                    existingUser.setName(userDTO.getName());
                    existingUser.setEmail(userDTO.getEmail());
                    return userMongoRepository.save(existingUser)
                            .map(Converter::entityToDto);
                });
    }

    @Override
    public Mono<Void> delete(String userId) {
        return userMongoRepository.deleteById(userId);
    }
}
