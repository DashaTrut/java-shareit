package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@SuperBuilder
@NoArgsConstructor
public class ItemDto {

    private int id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    private ItemRequest request;


    public ItemDto(String name, String description, boolean available) {
        this.available = available;
        this.name = name;
        this.description = description;
    }

    public ItemDto(String name, String description, boolean available, Integer integer) {
    }
}

