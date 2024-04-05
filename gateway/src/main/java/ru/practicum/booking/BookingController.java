package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.State;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(name = "state", defaultValue = "ALL") String state,
                                              @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        State state1 = State.getEnumValue(state);
        log.info("Get booking with state {}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingClient.getBookings(userId, state1, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestBody @Valid BookingDto requestDto,
                                           @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.bookItem(userId, requestDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @PatchMapping("{bookingId}")
    public ResponseEntity<Object> updateStatusBooking(@PathVariable Integer bookingId, @RequestParam Boolean approved,
                                                      @RequestHeader("X-Sharer-User-Id") Integer id) {
        log.info("Update status booking{}", bookingId);

        return bookingClient.updateStatusBooking(bookingId, approved, id);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingForOwnerAndState(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                             @RequestParam(defaultValue = "ALL", required = false) String state,
                                                             @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
                                                             @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        State state1 = State.getEnumValue(state);
        log.info("Get booking with state and owner {}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingClient.getBookingForOwner(userId, state1, from, size);
    }
}
