package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class CommentMapper {
    public Comment toComment(CommentDto text, User user, Item item) {
        return Comment.builder()
                .text(text.getText())
                .author(user)
                .item(item)
                .created(LocalDateTime.now())
                .build();
    }

    public CommentForItem toCommentForItem(Comment comment) {
        return CommentForItem.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorId(comment.getAuthor().getId())
                .authorName(comment.getAuthor().getName())
                .itemId(comment.getItem().getId())
                .created(comment.getCreated())
                .build();
    }

    public Set<CommentForItem> toSetCommentForItem(Set<Comment> set) {
        Set<CommentForItem> result = new HashSet<>();
        for (Comment comment : set) {
            result.add(toCommentForItem(comment));
        }
        return result;
    }
}

