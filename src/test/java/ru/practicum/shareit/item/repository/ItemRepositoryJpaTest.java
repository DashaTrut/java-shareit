package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepositoryJpa;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRepositoryJpaTest {
    @Autowired
    private UserRepositoryJpa userRepository;
    @Autowired
    private ItemRepositoryJpa itemRepository;
    @Autowired
    private RequestRepositoryJpa requestRepository;

    private User user;
    private Item item;
    private ItemRequest itemRequest;

    @BeforeEach
    private void addItem() {
        user = userRepository.save(User.builder()
                .email("test@email.com")
                .name("lasa")
                .build());
        itemRequest = requestRepository.save(ItemRequest.builder()
                .created(LocalDateTime.now())
                .description("test request")
                .requester(user)
                .build());

        item = itemRepository.save(Item.builder()
                .name("item")
                .owner(user)
                .available(true)
                .description("test item")
                .request(itemRequest.getId())
                .tags(Collections.EMPTY_SET)
                .build());
    }

    @Test
    void search() {
        String text = "test";
        List<Item> actualItem = itemRepository.search(text);
        assertTrue(!actualItem.isEmpty());
    }

    @Test
    void findByOwnerId() {
        Collection<Item> actualItem = itemRepository.findByOwnerId(user.getId());
        assertTrue(!actualItem.isEmpty());
    }

    @Test
    void findAllByRequest() {
        Collection<Item> actualItem = itemRepository.findAllByRequest(1);
        assertTrue(!actualItem.isEmpty());
    }
}