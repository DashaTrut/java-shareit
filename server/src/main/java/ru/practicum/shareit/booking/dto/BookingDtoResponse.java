package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDtoResponseForBooking;
import ru.practicum.shareit.user.dto.UserDtoResponse;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class BookingDtoResponse {

    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemDtoResponseForBooking item;
    private UserDtoResponse booker;
    private Status status;

}
