package ru.practicum.shareit.request;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@SuperBuilder
public class ItemRequest { //класс, отвечающий за запрос вещи
    @NotBlank
    private Integer id;
    @NotBlank
    private String description;
    private User requestor;
    private LocalDateTime created;
    //только тут сделать валидацию
}
