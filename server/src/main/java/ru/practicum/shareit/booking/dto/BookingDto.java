package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private Integer itemId;

    private LocalDateTime start;

    private LocalDateTime end;
}
