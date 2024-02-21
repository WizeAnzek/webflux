package com.prova.webflux.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class GetUsersResponseDTO {
    private List<GetUserResponseDTO> users;

    @Data
    public static class GetUserResponseDTO {
        private String name;
        private String email;
    }
}
