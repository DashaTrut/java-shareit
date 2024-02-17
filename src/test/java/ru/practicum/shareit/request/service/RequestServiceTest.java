package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
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
    void getRequest_whenRequestDtoWithFeedbackItem() {
        int userId = 1;
        int requestId = 1;
        LocalDateTime time = LocalDateTime.now().minusHours(1);
        User userToSave = new User(userId, "first name", "set@email.com");
        ItemRequest itemRequest = new ItemRequest(requestId, "want item", userToSave, time);
        ItemDto itemDto = new ItemDto(1, "item name", "item description", true, requestId);
        Item item = new Item(1, "item name", userToSave, "item description", true, requestId, Collections.EMPTY_SET);

        RequestDtoWithFeedbackItem requestDtoWithFeedbackItem = new RequestDtoWithFeedbackItem(1, "want item", time, List.of(itemDto));

        when(userRepositoryJpa.findById(userId)).thenReturn(Optional.of(userToSave));
        when(requestRepositoryJpa.findById(requestId)).thenReturn(Optional.of(itemRequest));
        when(itemRepositoryJpa.findAllByRequest(requestId)).thenReturn(List.of(item));
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
    void getRequestUserAll_whenSetRequestDtoWithFeedbackItem() {
        int userId = 1;
        int requestId = 1;
        LocalDateTime time = LocalDateTime.now().minusHours(1);
        User userToSave = new User(userId, "first name", "set@email.com");
        ItemDto itemDto = new ItemDto(1, "item name", "item description", true, requestId);
        Item item = new Item(1, "item name", userToSave, "item description", true, requestId, Collections.EMPTY_SET);
        ItemRequest itemRequest = new ItemRequest(requestId, "want item", userToSave, time);
        RequestDtoWithFeedbackItem requestDtoWithFeedbackItem = new RequestDtoWithFeedbackItem(1, "want item", time, List.of(itemDto));

        when(userRepositoryJpa.findById(userId)).thenReturn(Optional.of(userToSave));
        when(requestRepositoryJpa.findAllByRequestorId(userId)).thenReturn(Set.of(itemRequest));
        when(itemRepositoryJpa.findAllByRequest(requestId)).thenReturn(List.of(item));
        Set<RequestDtoWithFeedbackItem> actualItemRequest = requestService.getRequestUserAll(userId);

        assertNotNull(actualItemRequest);
        assertEquals(Set.of(requestDtoWithFeedbackItem), actualItemRequest);
    }

    @Test
    void getRequestUserAll_whenThrow() {
        int userId = 1;

        when(userRepositoryJpa.findById(userId)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> requestService.getRequestUserAll(userId));

        verify(itemRepositoryJpa, never()).findAllByRequest(anyInt());
    }

    @Test
    void getRequestAllPage_whenListRequestDtoWithFeedbackItem() {
        int userId = 1;
        int requestId = 1;
        LocalDateTime time = LocalDateTime.now().minusHours(1);
        User userToSave = new User(userId, "first name", "set@email.com");
        ItemDto itemDto = new ItemDto(1, "item name", "item description", true, requestId);
        Item item = new Item(1, "item name", userToSave, "item description", true, requestId, Collections.EMPTY_SET);
        Item item2 = new Item(2, "item name two", userToSave, "item description", true, requestId, Collections.EMPTY_SET);
        ItemRequest itemRequest = new ItemRequest(requestId, "want item", userToSave, time);
        RequestDtoWithFeedbackItem requestDtoWithFeedbackItem = new RequestDtoWithFeedbackItem(1, "want item", time, List.of(itemDto, ItemMapper.toItemDto(item2)));

        when(userRepositoryJpa.findById(userId)).thenReturn(Optional.of(userToSave));
        when(requestRepositoryJpa.findAllByRequestorIdNot(userId)).thenReturn(List.of(itemRequest));
        when(itemRepositoryJpa.findAllByRequest(requestId)).thenReturn(List.of(item, item2));
        List<RequestDtoWithFeedbackItem> actualItemRequest = requestService.getRequestAllPage(userId, null, null);

        assertNotNull(actualItemRequest);
        assertEquals(List.of(requestDtoWithFeedbackItem), actualItemRequest);
        verify(requestRepositoryJpa, never()).findAllByRequestorIdNot(anyInt(), any(PageRequest.class));
    }

    @Test
    void getRequestAllPage_whenListRequestDtoWithFeedbackItemWithPage() {
        int userId = 1;
        int requestId = 1;
        int requestId2 = 2;
        LocalDateTime time = LocalDateTime.now().minusHours(1);
        User userToSave = new User(userId, "first name", "set@email.com");
        ItemDto itemDto = new ItemDto(1, "item name", "item description", true, requestId);
        Item item = new Item(1, "item name", userToSave, "item description", true, requestId, Collections.EMPTY_SET);
        Item item2 = new Item(2, "item name two", userToSave, "item description", true, requestId, Collections.EMPTY_SET);
        ItemRequest itemRequest = new ItemRequest(requestId, "want item", userToSave, time);
        RequestDtoWithFeedbackItem requestDtoWithFeedbackItem = new RequestDtoWithFeedbackItem(1, "want item", time, List.of(itemDto, ItemMapper.toItemDto(item2)));

        when(userRepositoryJpa.findById(userId)).thenReturn(Optional.of(userToSave));
        when(requestRepositoryJpa.findAllByRequestorIdNot(anyInt(), any(PageRequest.class))).thenReturn(List.of(itemRequest));
        when(itemRepositoryJpa.findAllByRequest(requestId)).thenReturn(List.of(item, item2));
        List<RequestDtoWithFeedbackItem> actualItemRequest = requestService.getRequestAllPage(userId, 1, 1);

        assertNotNull(actualItemRequest);
        assertEquals(List.of(requestDtoWithFeedbackItem), actualItemRequest);
    }

    @Test
    void getRequestAllPage_whenThrow() {
        int userId = 1;

        when(userRepositoryJpa.findById(userId)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> requestService.getRequestAllPage(userId, null, null));

        verify(requestRepositoryJpa, never()).findAllByRequestorIdNot(anyInt());
    }

}