package ru.practicum.shareit.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.*;


/**
 * TODO Sprint add-controllers.
 */
@Data
@SuperBuilder
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
