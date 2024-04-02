package ru.practicum.shareit.request.service;

import org.hibernate.exception.SQLGrammarException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepositoryJpa;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDtoWithFeedbackItem;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepositoryJpa;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestServiceTest {
    @Mock
    private RequestRepositoryJpa requestRepositoryJpa;
    @Mock
    private UserRepositoryJpa userRepositoryJpa;
    @Mock
    private ItemRepositoryJpa itemRepositoryJpa;

    @InjectMocks
    private RequestService requestService;

    private Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "created");

    @Test
    void create_whenRequestSave() {
        int userId = 1;
        User userToSave = new User(userId, "first name", "set@email.com");
        ItemRequest itemRequest = new ItemRequest(1, "want item", userToSave, LocalDateTime.now().minusHours(1));
        ItemRequestDto itemRequestDto = new ItemRequestDto("want item", LocalDateTime.now().minusHours(1));

        when(userRepositoryJpa.findById(userId)).thenReturn(Optional.of(userToSave));
        when(requestRepositoryJpa.save(any(ItemRequest.class))).thenReturn(itemRequest);
        ItemRequest actualItemRequest = requestService.create(itemRequestDto, userId);

        assertEquals(itemRequest, actualItemRequest);
        verify(requestRepositoryJpa).save(any(ItemRequest.class));
    }

    @Test
    void create_whenThrowUser() {
        int userId = 1;
        ItemRequestDto itemRequestDto = new ItemRequestDto("want item", LocalDateTime.now().minusHours(1));

        when(userRepositoryJpa.findById(userId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> requestService.create(itemRequestDto, userId));

        verify(requestRepositoryJpa, never()).save(any(ItemRequest.class));
    }


    @Test
    void create_whenThrowSave() {
        int userId = 1;
        User userToSave = new User(userId, "first name", "set@email.com");
        ItemRequestDto itemRequestDto = new ItemRequestDto("want item", LocalDateTime.now().minusHours(1));


        when(userRepositoryJpa.findById(userId)).thenReturn(Optional.of(userToSave));
        when(requestRepositoryJpa.save(any(ItemRequest.class))).thenThrow(SQLGrammarException.class);
        assertThrows(SQLGrammarException.class, () -> requestService.create(itemRequestDto, userId));
    }

    @Test
    void getRequest_whenRequestDtoWithFeedbackItem() {
        int userId = 1;
        int requestId = 1;
        LocalDateTime time = LocalDateTime.now().minusHours(1);
        User userToSave = new User(userId, "first name", "set@email.com");
        ItemRequest itemRequest = new ItemRequest(requestId, "want item", userToSave, time);
        ItemDto itemDto = new ItemDto(1, "item name", "item description", true, requestId);
        Item item = new Item(1, "item name", userToSave, "item description", true, itemRequest, Collections.EMPTY_SET);

        RequestDtoWithFeedbackItem requestDtoWithFeedbackItem = new RequestDtoWithFeedbackItem(1, "want item", time, List.of(itemDto));

        when(userRepositoryJpa.findById(userId)).thenReturn(Optional.of(userToSave));
        when(requestRepositoryJpa.findById(requestId)).thenReturn(Optional.of(itemRequest));
        when(itemRepositoryJpa.findAllByRequestId(requestId)).thenReturn(List.of(item));
        RequestDtoWithFeedbackItem actualItemRequest = requestService.getRequest(requestId, userId);

        assertEquals(requestDtoWithFeedbackItem, actualItemRequest);
    }

    @Test
    void getRequest_whenThrow() {
        int userId = 1;
        int requestId = 1;
        User userToSave = new User(userId, "first name", "set@email.com");

        when(userRepositoryJpa.findById(userId)).thenReturn(Optional.of(userToSave));
        when(requestRepositoryJpa.findById(requestId)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> requestService.getRequest(requestId, userId));
    }

    @Test
    void getRequest_whenThrowUser() {
        int userId = 1;
        int requestId = 1;

        when(userRepositoryJpa.findById(userId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> requestService.getRequest(requestId, userId));
    }

    @Test
    void getRequest_whenThrowItem() {
        LocalDateTime time = LocalDateTime.now().minusHours(1);
        int userId = 1;
        int requestId = 1;
        User userToSave = new User(userId, "first name", "set@email.com");
        ItemRequest itemRequest = new ItemRequest(requestId, "want item", userToSave, time);

        when(userRepositoryJpa.findById(userId)).thenReturn(Optional.of(userToSave));
        when(requestRepositoryJpa.findById(requestId)).thenReturn(Optional.of(itemRequest));
        when(itemRepositoryJpa.findAllByRequestId(requestId)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> requestService.getRequest(requestId, userId));
    }

    @Test
    void getRequestUserAll_whenThrow() {
        int userId = 1;

        when(userRepositoryJpa.findById(userId)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> requestService.getRequestUserAll(userId));

        verify(itemRepositoryJpa, never()).findAllByRequestId(anyInt());
    }

    @Test
    void getRequestAllPage_whenListRequestDtoWithFeedbackItem() {
        int userId = 1;
        int requestId = 1;
        LocalDateTime time = LocalDateTime.now().minusHours(1);
        User userToSave = new User(userId, "first name", "set@email.com");
        ItemRequest itemRequest = new ItemRequest(requestId, "want item", userToSave, time);
        Item item = new Item(1, "item name", userToSave, "item description", true, itemRequest, Collections.EMPTY_SET);
        Item item2 = new Item(2, "item name two", userToSave, "item description", true, itemRequest, Collections.EMPTY_SET);
        RequestDtoWithFeedbackItem requestDtoWithFeedbackItem = new RequestDtoWithFeedbackItem(1, "want item", time, ItemMapper.mapToItemDto(List.of(item, item2)));

        when(userRepositoryJpa.findById(userId)).thenReturn(Optional.of(userToSave));
        when(requestRepositoryJpa.findAllByRequesterIdNot(
                anyInt(), any(Pageable.class))).thenReturn(List.of(itemRequest));
        when(itemRepositoryJpa.findByRequestIdIn(any())).thenReturn(List.of(item, item2));
        List<RequestDtoWithFeedbackItem> actualItemRequest = requestService.getRequestAllPage(userId, 0, 10);

        assertNotNull(actualItemRequest);
        assertEquals(List.of(requestDtoWithFeedbackItem), actualItemRequest);

    }


    @Test
    void getRequestAllPage_whenThrow() {
        int userId = 1;

        when(userRepositoryJpa.findById(userId)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> requestService.getRequestAllPage(userId, 0, 1));

        verify(requestRepositoryJpa, never()).findAllByRequesterIdNot(anyInt(), any(Pageable.class));
    }

    @Test
    void getRequestAllPage_whenThrowNull() {
        int userId = 1;

        when(userRepositoryJpa.findById(userId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> requestService.getRequestAllPage(userId, 0, 1));

        verify(requestRepositoryJpa, never()).findAllByRequesterIdNot(anyInt(), any(Pageable.class));
    }


    @Test
    void getRequestAllPage_whenThrowRequest() {
        int userId = 1;
        User userToSave = new User(userId, "first name", "set@email.com");

        when(userRepositoryJpa.findById(userId)).thenReturn(Optional.of(userToSave));
        when(requestRepositoryJpa.findAllByRequesterIdNot(anyInt(), any(Pageable.class))).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> requestService.getRequestAllPage(userId, 0, 1));
    }

    @Test
    void getRequestAllPage_whenThrowFindAllByRequest() {
        int userId = 1;
        int requestId = 1;
        LocalDateTime time = LocalDateTime.now().minusHours(1);
        User userToSave = new User(userId, "first name", "set@email.com");
        ItemDto itemDto = new ItemDto(1, "item name", "item description", true, requestId);
        ItemRequest itemRequest = new ItemRequest(requestId, "want item", userToSave, time);
        Item item2 = new Item(2, "item name two", userToSave, "item description", true, itemRequest, Collections.EMPTY_SET);
        RequestDtoWithFeedbackItem requestDtoWithFeedbackItem = new RequestDtoWithFeedbackItem(1, "want item", time, List.of(itemDto, ItemMapper.toItemDto(item2)));

        when(userRepositoryJpa.findById(userId)).thenReturn(Optional.of(userToSave));
        when(requestRepositoryJpa.findAllByRequesterIdNot(anyInt(), any(PageRequest.class))).thenReturn(List.of(itemRequest));
        when(itemRepositoryJpa.findByRequestIdIn(any())).thenThrow(SQLGrammarException.class);
        assertThrows(SQLGrammarException.class, () -> requestService.getRequestAllPage(userId, 1, 2));
    }
}