package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepositoryJpa;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDtoWithFeedbackItem;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepositoryJpa;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepositoryJpa requestRepositoryJpa;
    private final UserRepositoryJpa userRepositoryJpa;
    private final ItemRepositoryJpa itemRepositoryJpa;


    public ItemRequest create(ItemRequestDto itemRequestDto, int id) {
        User user = userRepositoryJpa.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Пользователя не существует"));
        return requestRepositoryJpa.save(RequestMapper.toItemRequest(itemRequestDto, user));
    }

    public Set<RequestDtoWithFeedbackItem> getRequestUserAll(int idUser) {
        User user = userRepositoryJpa.findById(idUser).orElseThrow(() ->
                new EntityNotFoundException("Пользователя не существует"));
        Set<RequestDtoWithFeedbackItem> result = new HashSet<>();
        Collection<ItemRequest> set = requestRepositoryJpa.findAllByRequestorId(idUser);
        if (!set.isEmpty()) {
            for (ItemRequest itemRequest : set) {
                Collection<Item> items = itemRepositoryJpa.findAllByRequest(itemRequest.getId());
                result.add(RequestMapper.toRequestDtoWithFeedbackItem(itemRequest, ItemMapper.mapToItemDto(items)));
            }
        }
        return result;
    }

    public RequestDtoWithFeedbackItem getRequest(int requestId, int idUser) {
        User user = userRepositoryJpa.findById(idUser).orElseThrow(() ->
                new EntityNotFoundException("Пользователя не существует"));
        ItemRequest itemRequest = requestRepositoryJpa.findById(requestId).orElseThrow(() ->
                new EntityNotFoundException("Пользователя не существует"));
        Collection<Item> items = itemRepositoryJpa.findAllByRequest(itemRequest.getId());
        return RequestMapper.toRequestDtoWithFeedbackItem(itemRequest, ItemMapper.mapToItemDto(items));
    }


    public List<RequestDtoWithFeedbackItem> getRequestAllPage(int idUser, Integer page, Integer size) {
        User user = userRepositoryJpa.findById(idUser).orElseThrow(() ->
                new EntityNotFoundException("Пользователя не существует"));
        List<RequestDtoWithFeedbackItem> result = new ArrayList<>();
        List<ItemRequest> list;
        if (page == null && size == null) {
            list = requestRepositoryJpa.findAllByRequestorIdNot(idUser);
        } else {
            list = requestRepositoryJpa.findAllByRequestorIdNot(
                    idUser, PageRequest.of(page, size, Sort.Direction.DESC, "created"));
        }
        for (ItemRequest itemRequest : list) {
            List<ItemDto> items = ItemMapper.mapToItemDto(itemRepositoryJpa.findAllByRequest(itemRequest.getId()));
            result.add(RequestMapper.toRequestDtoWithFeedbackItem(itemRequest, items));
        }
        return result;
    }

    public List<RequestResponseDto> getRequestAll(@RequestHeader("X-Sharer-User-Id") Integer idUser) {
        return RequestMapper.toListRequestResponseDto(
                requestRepositoryJpa.findAll());
    }
}




