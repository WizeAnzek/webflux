package com.prova.webflux.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import com.prova.webflux.dto.response.GetUsersResponseDTO.GetUserResponseDTO;

@Data
public class GetBookingsResponseDTO {
    private List<GetBookingResponseDTO> list;
    @Data
    public static class GetBookingResponseDTO {
        private LocalDate date;
        private LocalTime time;
        private GetUserResponseDTO user;
    }
}
