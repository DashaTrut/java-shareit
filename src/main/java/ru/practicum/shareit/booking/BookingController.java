package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingMapper;
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
    public BookingDtoResponse addBooking(@Valid @RequestBody BookingDto bookingDto, @RequestHeader("X-Sharer-User-Id") Integer id) {
        log.info("Add booking{}", bookingDto);
        return BookingMapper.toBookingDtoResponse(bookingServiceIpl.create(bookingDto, id));

    }

    @PatchMapping("{bookingId}")
    public BookingDtoResponse updateStatusBooking(@PathVariable Integer bookingId, @RequestParam Boolean approved,
                                                  @RequestHeader("X-Sharer-User-Id") Integer id) {
        log.info("Update status booking{}", bookingId);

        return BookingMapper.toBookingDtoResponse(bookingServiceIpl.updateStatusBooking(bookingId, approved, id));
    }

    @GetMapping("{bookingId}")
    public BookingDtoResponse getBooking(@PathVariable Integer bookingId, @RequestHeader("X-Sharer-User-Id") Integer idUser) {
        return BookingMapper.toBookingDtoResponse(bookingServiceIpl.getBooking(bookingId, idUser));
    }

    @GetMapping
    public Collection<BookingDtoResponse> getBookingForState(@RequestHeader("X-Sharer-User-Id") Integer idUser,
                                                             @RequestParam(defaultValue = "ALL") String state) {
        return BookingMapper.mapToBookingDtoResponse(bookingServiceIpl.getBookingForState(idUser, state));
    }

    @GetMapping("/owner")
    public Collection<BookingDtoResponse> getBookingForOwnerAndState(@RequestHeader("X-Sharer-User-Id") Integer idUser,
                                                                     @RequestParam(defaultValue = "ALL") String state) {
        return BookingMapper.mapToBookingDtoResponse(bookingServiceIpl.getBookingForOwnerAndState(idUser, state));
    }

}
