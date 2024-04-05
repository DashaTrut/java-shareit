package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoBookingTest {

    @Autowired
    private JacksonTester<ItemDtoBooking> json;

    @SneakyThrows
    @Test
    void testItemDto() {

        ItemDtoBooking itemDto = ItemDtoBooking.builder()
                .id(1)
                .name("item")
                .available(true)
                .description("test item")
                .request(10)
                .comments(Collections.EMPTY_SET)
                .build();

        JsonContent<ItemDtoBooking> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("item");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("test item");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.request").isEqualTo(10);
    }
}