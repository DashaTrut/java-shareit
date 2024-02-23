package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StateTest {

    @Test
    void getEnumValue() {
        String stateStr = "Unknown";
        String finalStateStr = stateStr;
        assertThrows(EntityNotFoundException.class, () -> State.getEnumValue(finalStateStr));

        stateStr = "ALL";
        State stateTest = State.getEnumValue(stateStr);
        assertEquals(stateTest, State.ALL);

        stateStr = "CURRENT";
        stateTest = State.getEnumValue(stateStr);
        assertEquals(stateTest, State.CURRENT);

        stateStr = "PAST";
        stateTest = State.getEnumValue(stateStr);
        assertEquals(stateTest, State.PAST);

        stateStr = "FUTURE";
        stateTest = State.getEnumValue(stateStr);
        assertEquals(stateTest, State.FUTURE);

        stateStr = "REJECTED";
        stateTest = State.getEnumValue(stateStr);
        assertEquals(stateTest, State.REJECTED);

        stateStr = "WAITING";
        stateTest = State.getEnumValue(stateStr);
        assertEquals(stateTest, State.WAITING);
    }
}