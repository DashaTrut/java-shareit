package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepositoryJpa;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RequestRepositoryJpaTest {

    @Autowired
    private UserRepositoryJpa userRepository;
    @Autowired
    private ItemRepositoryJpa itemRepository;
    @Autowired
    private RequestRepositoryJpa requestRepository;

    private User user;
    private Item item;
    private ItemRequest itemRequest;
    private User user1;
    private User user2;
    private ItemRequest itemRequest1;

    @BeforeEach
    private void addItem() {
        user = userRepository.save(User.builder()
                .email("test@email.com")
                .name("lasa")
                .build());

        itemRequest = requestRepository.save(ItemRequest.builder()
                .created(LocalDateTime.now())
                .description("test request")
                .requestor(user)
                .build());
        user1 = userRepository.save(User.builder()
                .email("trut@email.com")
                .name("trut")
                .build());

        user2 = userRepository.save(User.builder()
                .email("t@email.com")
                .name("t")
                .build());

        itemRequest1 = requestRepository.save(ItemRequest.builder()
                .created(LocalDateTime.now())
                .description("test request two")
                .requestor(user1)
                .build());

        itemRepository.save(Item.builder()
                .name("item")
                .owner(user)
                .available(true)
                .description("test item")
                .tags(Collections.EMPTY_SET)
                .build());
    }

    @Test
    void findAllByRequestorId() {
        Collection<ItemRequest> actualItemRequest = requestRepository.findAllByRequestorId(user1.getId());
        assertTrue(!actualItemRequest.isEmpty());
        assertEquals(actualItemRequest.size(), 1);
        assertEquals(actualItemRequest, List.of(itemRequest1));
    }

    @Test
    void findAllByRequestorIdNot() {
        List<ItemRequest> actualItemRequest = requestRepository.findAllByRequestorIdNot(user1.getId());
        assertTrue(!actualItemRequest.isEmpty());
        assertEquals(actualItemRequest.size(), 1);
        assertEquals(actualItemRequest, List.of(itemRequest));
    }

    @Test
    void findAllByRequestorIdNotPage() {
        List<ItemRequest> actualItemRequest = requestRepository.findAllByRequestorIdNot(user2.getId(), PageRequest.of(1, 1, Sort.Direction.DESC, "created"));
        assertTrue(!actualItemRequest.isEmpty());
        assertEquals(actualItemRequest.size(), 1);
        assertEquals(actualItemRequest, List.of(itemRequest));
    }
}