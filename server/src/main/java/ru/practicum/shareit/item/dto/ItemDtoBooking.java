package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;

import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
public class ItemDtoBooking {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private Integer request;
    private BookingDtoForItem lastBooking;
    private BookingDtoForItem nextBooking;
    private Collection<CommentForItem> comments;

}

