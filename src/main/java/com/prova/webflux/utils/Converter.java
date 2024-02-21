package com.prova.webflux.utils;

import com.prova.webflux.domains.Booking;
import com.prova.webflux.domains.User;
import com.prova.webflux.dto.BookingDTO;
import com.prova.webflux.dto.UserDTO;
import org.springframework.beans.BeanUtils;

public class Converter {

    public static Booking dtoToEntity(BookingDTO bookingDTO) {
        Booking booking = new Booking();
        BeanUtils.copyProperties(bookingDTO, booking);
        return booking;
    }

    public static BookingDTO entityToDto(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        BeanUtils.copyProperties(booking, bookingDTO);
        return bookingDTO;
    }

    public static User dtoToEntity(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return user;
    }

    public static UserDTO entityToDto(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }
}
