package ru.practicum.shareit.booking.dto;

import lombok.*;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class BookingDto {
    private Integer itemId;
    @NotNull
    @Future
    private LocalDateTime start;
    @NotNull
    @Future
    private LocalDateTime end;
}
