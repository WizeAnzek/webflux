package com.prova.webflux.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class BookingDTO {
    private String id;
    private LocalTime time;
    private LocalDate date;
    private String userId;
}
