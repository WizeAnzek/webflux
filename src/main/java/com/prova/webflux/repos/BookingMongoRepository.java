package com.prova.webflux.repos;

import com.prova.webflux.domains.Booking;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BookingMongoRepository extends ReactiveMongoRepository<Booking, String> {
}
