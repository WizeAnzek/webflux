package com.prova.webflux.services.impl;

import com.prova.webflux.domains.User;
import com.prova.webflux.dto.UserDTO;
import com.prova.webflux.repos.UserMongoRepository;
import com.prova.webflux.services.api.IUserService;
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
                .map(user -> modelMapper.map(user, UserDTO.class));

    }

    @Override
    public Mono<UserDTO> save(UserDTO userDTO) {
        User user = new User(userDTO.getName(), userDTO.getEmail());
        return userMongoRepository.save(user)
                .map(savedUser -> modelMapper.map(savedUser, UserDTO.class));
    }

    public Mono<UserDTO> findById(String userId) {
        return userMongoRepository.findById(userId)
                .map(user -> modelMapper.map(user, UserDTO.class));
    }

    @Override
    public Mono<UserDTO> update(String userId, UserDTO userDTO) {
        return userMongoRepository.findById(userId)
                .flatMap(existingUser -> {
                    existingUser.setName(userDTO.getName() == null ? existingUser.getName() : userDTO.getName());
                    existingUser.setEmail(userDTO.getEmail() == null ? existingUser.getEmail() : userDTO.getEmail());
                    return userMongoRepository.save(existingUser)
                            .map(user -> modelMapper.map(user, UserDTO.class));
                });
    }

    @Override
    public Mono<Void> delete(String userId) {
        return userMongoRepository.deleteById(userId);
    }
}
