package com.prova.webflux.repos;

import com.prova.webflux.domains.Booking;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface BookingMongoRepository extends ReactiveMongoRepository<Booking, String> {

    Flux<Booking> findBookingsByDate(LocalDate bookingDate);
    @Query("{'date': {$gte: ?0, $lte: ?1}}")
    Flux<Booking> findBookingsByDateBetween(LocalDate fromDate, LocalDate toDate);
}
