package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;


@Getter
@Setter
@NoArgsConstructor
public class User {

    private Integer id;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String name;

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
