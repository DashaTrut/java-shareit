package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;


@Builder
@Getter
@Setter
@Entity
@ToString
@Table(name = "requests", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemRequest { //класс, отвечающий за запрос вещи
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;
    @Column(name = "description", nullable = false)
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;
    private LocalDateTime created;
}
