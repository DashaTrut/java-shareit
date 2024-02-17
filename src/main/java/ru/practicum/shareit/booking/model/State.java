package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.exception.EntityNotFoundException;

public enum State {
    ALL, // (англ. «все»).
    CURRENT, // (англ. «текущие»),
    PAST, // (англ. «завершённые»),
    FUTURE, // (англ. «будущие»),
    WAITING, // (англ. «ожидающие подтверждения»),
    REJECTED; // (англ. «отклонённые»)

    public static State getEnumValue(String state) {
        try {
            return State.valueOf(state);
        } catch (Exception e) {
            throw new EntityNotFoundException("Unknown state: " + state);
        }
    }
}