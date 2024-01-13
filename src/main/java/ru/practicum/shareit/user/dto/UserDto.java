package ru.practicum.shareit.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

@Data
@SuperBuilder
@NoArgsConstructor
public class UserDto {
    private Integer id;
    @NotBlank
    private String name;

    public UserDto(String name, Integer id) {
    }
}
