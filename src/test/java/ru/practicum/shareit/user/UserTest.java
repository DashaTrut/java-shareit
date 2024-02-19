package ru.practicum.shareit.user;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserTest {
    @Autowired
    private JacksonTester<User> json;

    @SneakyThrows
    @Test
    void testUser() {
        String email = "dasha@yandex.com";
        String name = "dasha";
        User user = User.builder()
                .id(1)
                .email(email)
                .name(name)
                .build();
        JsonContent<User> result = json.write(user);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(email);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(name);
    }

    @Test
    void testEquals() {
    }

    @Test
    void testHashCode() {
    }
}