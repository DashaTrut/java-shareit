package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@SuperBuilder
@NoArgsConstructor
public class Item {

    private Integer id;
    @NotBlank
    private String name;
    @NotNull
    private User owner;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;

    private ItemRequest request;
}
