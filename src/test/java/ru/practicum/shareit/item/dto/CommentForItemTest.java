package ru.practicum.shareit.item.dto;

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
class CommentForItemTest {

    @Autowired
    private JacksonTester<CommentForItem> json;

    @SneakyThrows
    @Test
    void testCommentForItem() {
        String name = "Dasha";
        String text = "hello item";
        int itemId = 10;
        int authorId = 15;
        LocalDateTime time = LocalDateTime.of(2023, 8, 7, 12, 30);


        CommentForItem commentDto = CommentForItem.builder()
                .id(1)
                .text(text)
                .itemId(itemId)
                .authorId(authorId)
                .authorName(name)
                .created(time)
                .build();

        JsonContent<CommentForItem> result = json.write(commentDto);

        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(text);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo(name);
        assertThat(result).extractingJsonPathNumberValue("$.authorId").isEqualTo(authorId);
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(itemId);
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));

    }
}