package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;

import java.util.Collection;

public interface CommentRepositoryJpa extends JpaRepository<Comment, Integer> {

    Collection<Comment> findAllByItemOwnerId(Integer ownerId);
}
