package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
public class RequestDtoWithFeedbackItem {
    @NotNull
    private int id;
    @NotBlank
    private String description;
    @NotNull
    private LocalDateTime created;
    private List<ItemDto> items;

}

