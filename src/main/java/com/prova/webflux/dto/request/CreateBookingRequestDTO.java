package com.prova.webflux.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreateBookingRequestDTO {
    private LocalTime time;
    private LocalDate date;
    private String userId;
}
