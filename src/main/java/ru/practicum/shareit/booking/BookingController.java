package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingServiceImpl;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingServiceImpl bookingServiceIpl;


    @PostMapping //добавление бронирование;
    public Booking addBooking(@Valid @RequestBody BookingDto bookingDto, @RequestHeader("X-Sharer-User-Id") Integer id) {
        log.info("Add booking{}", bookingDto);
        return bookingServiceIpl.create(bookingDto, id);

    }

    @PatchMapping("{bookingId}")
    public Booking updateStatusBooking(@PathVariable Integer bookingId, @RequestParam Boolean approved,
                                       @RequestHeader("X-Sharer-User-Id") Integer id) {
        log.info("Update status booking{}", bookingId);
        Status status;
        if (approved) {
            status = Status.APPROVED;
        } else {
            status = Status.REJECTED;
        }
        return bookingServiceIpl.updateStatusBooking(bookingId, status, id);
    }

    @GetMapping("{bookingId}")
    public Booking getBooking(@PathVariable Integer bookingId, @RequestHeader("X-Sharer-User-Id") Integer idUser) {
        return bookingServiceIpl.getBooking(bookingId, idUser);
    }

    @GetMapping
    public Collection<Booking> getBookingForState(@RequestHeader("X-Sharer-User-Id") Integer idUser,
                                                  @RequestParam(defaultValue = "ALL") String state) {
        return bookingServiceIpl.getBookingForState(idUser, state);
    }

    @GetMapping("/owner")
    public Collection<Booking> getBookingForOwnerAndState(@RequestHeader("X-Sharer-User-Id") Integer idUser,
                                                          @RequestParam(defaultValue = "ALL") String state) {
        return bookingServiceIpl.getBookingForOwnerAndState(idUser, state);
    }

}
