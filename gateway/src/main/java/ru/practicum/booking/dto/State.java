package ru.practicum.booking.dto;


import ru.practicum.exception.ValidationException;

public enum State {
    ALL, // (англ. «все»).
    CURRENT, // (англ. «текущие»),
    PAST, // (англ. «завершённые»),
    FUTURE, // (англ. «будущие»),
    WAITING, // (англ. «ожидающие подтверждения»),
    REJECTED; // (англ. «отклонённые»)

    public static State getEnumValue(String stringState) {
        try {
            return State.valueOf(stringState);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}