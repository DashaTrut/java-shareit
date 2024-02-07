package ru.practicum.shareit.item.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentForItem {

    private Integer id;
    private String text;
    private Integer authorId;
    private String authorName;
    private Integer itemId;
    private LocalDateTime created;

}
