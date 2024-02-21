package com.prova.webflux.repos;

import com.prova.webflux.domains.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserMongoRepository extends ReactiveMongoRepository<User, String> {
}
