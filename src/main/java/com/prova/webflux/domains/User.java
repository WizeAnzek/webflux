package com.prova.webflux.domains;

import com.prova.webflux.dto.BookingDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;

@Document
@Data
@NoArgsConstructor
public class User {

    @Id
    private String id;
    private String name;
    private String email;


    public User(String name, String email) {
        this.name = name;
        this.email = email;

    }

}
