package ru.practicum.shareit.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoForItemTest {
    @Autowired
    private JacksonTester<BookingDtoForItem> json;

    @SneakyThrows
    @Test
    void testBookingDtoForItem() {
        LocalDateTime start = LocalDateTime.of(2023, 8, 5, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 8, 7, 0, 0);
        BookingDtoForItem bookingDto = BookingDtoForItem.builder()
                .id(1)
                .bookerId(2)
                .build();
        JsonContent<BookingDtoForItem> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(2);
    }
}