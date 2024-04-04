package ru.practicum.shareit.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDtoResponseForBooking;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDtoResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
class BookingDtoResponseTest {

    @Autowired
    private JacksonTester<BookingDtoResponse> json;

    @SneakyThrows
    @Test
    void testBookingDtoResponse() {
        LocalDateTime start = LocalDateTime.of(2023, 8, 5, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 8, 7, 0, 0);

        User user = User.builder()
                .id(1)
                .email("test@email.com")
                .name("lasa")
                .build();

        UserDtoResponse user1 = UserDtoResponse.builder()
                .id(1)
                .name("lasa")
                .build();

        ItemDtoResponseForBooking itemDto = ItemDtoResponseForBooking.builder()
                .id(1)
                .name("item")
                .build();

        BookingDtoResponse bookingOutDto = BookingDtoResponse.builder()
                .id(1)
                .start(start)
                .end(end)
                .status(Status.APPROVED)
                .booker(user1)
                .item(itemDto)
                .build();

        JsonContent<BookingDtoResponse> result = json.write(bookingOutDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo("lasa");
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("item");
    }

}