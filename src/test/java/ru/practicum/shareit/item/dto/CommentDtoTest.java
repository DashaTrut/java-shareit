package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;


import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
class CommentDtoTest {

    @Autowired
    private JacksonTester<CommentDto> json;

    @SneakyThrows
    @Test
    void testCommentDto() {
        String text = "hello item";

        CommentDto commentDto = new CommentDto(text);

        JsonContent<CommentDto> result = json.write(commentDto);

        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(text);
    }
}