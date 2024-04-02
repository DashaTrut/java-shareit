package ru.practicum.booking.dto;

import java.util.Optional;

public enum State {
    ALL, // (англ. «все»).
    CURRENT, // (англ. «текущие»),
    PAST, // (англ. «завершённые»),
    FUTURE, // (англ. «будущие»),
    WAITING, // (англ. «ожидающие подтверждения»),
    REJECTED; // (англ. «отклонённые»)

    public static Optional<State> getEnumValue(String stringState) {
        for (State state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}