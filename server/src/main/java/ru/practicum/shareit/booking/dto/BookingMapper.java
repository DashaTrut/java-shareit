package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDtoResponseForBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDtoResponse;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class BookingMapper {

    public BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .itemId(booking.getItem().getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .build();

    }


    public Booking toBooking(BookingDto bookingDto, User user, Item item) {
        return Booking.builder()
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(item)
                .booker(user)
                .status(Status.WAITING)
                .build();
    }


    public List<BookingDtoResponse> mapToBookingDtoResponse(Iterable<Booking> bookings) {
        List<BookingDtoResponse> result = new ArrayList<>();
        for (Booking booking : bookings) {
            result.add(toBookingDtoResponse(booking));
        }
        return result;
    }

    public BookingDtoForItem toBookingDtoForItem(Booking booking) {
        return BookingDtoForItem.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }

    public BookingDtoResponse toBookingDtoResponse(Booking booking) {
        return BookingDtoResponse.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(new ItemDtoResponseForBooking(booking.getItem().getId(), booking.getItem().getName()))
                .booker(new UserDtoResponse(booking.getBooker().getId(), booking.getBooker().getName()))
                .status(booking.getStatus())
                .build();
    }
}


