package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

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


    public List<BookingDto> mapToBookingDto(Iterable<Booking> bookings) {
        List<BookingDto> result = new ArrayList<>();
        for (Booking booking : bookings) {
            result.add(toBookingDto(booking));
        }
        return result;
    }

    public BookingDtoForItem toBookingDtoForItem(Booking booking) {
        return BookingDtoForItem.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }
}


