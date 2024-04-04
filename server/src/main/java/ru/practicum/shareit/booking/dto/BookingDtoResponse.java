package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDtoResponseForBooking;
import ru.practicum.shareit.user.dto.UserDtoResponse;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class BookingDtoResponse {
    @NotNull
    private int id;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
    @NotNull
    private ItemDtoResponseForBooking item;
    @NotNull
    private UserDtoResponse booker;
    @NotNull
    private Status status;

}
