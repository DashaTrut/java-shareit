package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface ItemRepositoryJpa extends JpaRepository<Item, Integer> {
    Collection<Item> findByOwnerId(Integer ownerId);

    List<Item> findByOwnerId(Integer ownerId, Pageable pageable);

    Collection<Item> findAllByRequestId(Integer requestId);

    @Query(" select i from Item i " +
            "where upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%'))" +
            " and i.available = true")
    List<Item> search(String text, Pageable pageable);

    List<Item> findByRequestIdIn(List<Integer> requestIds);


}
