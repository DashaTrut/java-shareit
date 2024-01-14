package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
public class UserDto {
    private Integer id;
    private String email;

    private String name;


    public UserDto(Integer id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
}
