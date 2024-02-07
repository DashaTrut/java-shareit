package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@ToString
@Table(name = "items", schema = "public")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_owner", nullable = false)
    private User owner;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;

    private Integer request;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Comment> tags = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(getId(), item.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


}
