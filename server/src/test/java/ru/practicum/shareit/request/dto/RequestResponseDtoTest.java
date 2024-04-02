package ru.practicum.shareit.request.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
class RequestResponseDtoTest {

    @Autowired
    private JacksonTester<RequestResponseDto> json;

    @SneakyThrows
    @Test
    void testRequestResponseDto() {
        int id = 1;
        LocalDateTime created = LocalDateTime.of(2023, 8, 7, 12, 30);
        String text = "i need a table";
        RequestResponseDto requestResponseDto = RequestResponseDto.builder()
                .id(id)
                .description(text)
                .created(created)
                .build();
        JsonContent<RequestResponseDto> result = json.write(requestResponseDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(text);
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(created.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    }
}