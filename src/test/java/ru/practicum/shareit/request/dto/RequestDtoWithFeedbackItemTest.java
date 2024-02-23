package ru.practicum.shareit.request.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class RequestDtoWithFeedbackItemTest {
    @Autowired
    private JacksonTester<RequestDtoWithFeedbackItem> json;

    @SneakyThrows
    @Test
    void testItemRequestDto() {
        int id = 1;
        LocalDateTime created = LocalDateTime.of(2023, 8, 7, 12, 30);
        List<ItemDto> items = Collections.EMPTY_LIST;
        String text = "i need a table";
        RequestDtoWithFeedbackItem itemRequestDto = RequestDtoWithFeedbackItem.builder()
                .id(id)
                .description(text)
                .created(created)
                .items(items)
                .build();
        JsonContent<RequestDtoWithFeedbackItem> result = json.write(itemRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(text);
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(created.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathArrayValue("$.items").isEqualTo(Collections.EMPTY_LIST);
    }
}