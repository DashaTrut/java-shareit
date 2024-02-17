package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;


@Builder
@Getter
@Setter
@Entity
@ToString
@Table(name = "requests", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest { //класс, отвечающий за запрос вещи
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestor_id", nullable = false)
    private User requestor;
    private LocalDateTime created;
    //только тут сделать валидацию
}
