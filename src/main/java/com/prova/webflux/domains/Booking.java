package com.prova.webflux.domains;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;
import java.time.LocalTime;

@Document
@Data
public class Booking {

    @Id
    private String id;
    private LocalDate date;
    private LocalTime time;
    @DocumentReference
    private User user;

}
