package ru.practicum.shareit.user;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;


@Getter
@Setter
@Builder
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return id != null && id.equals(((User) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
