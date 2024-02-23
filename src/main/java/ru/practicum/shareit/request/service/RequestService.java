package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepositoryJpa;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDtoWithFeedbackItem;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepositoryJpa;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

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


    public RequestDtoWithFeedbackItem getRequest(int requestId, int idUser) {
        User user = userRepositoryJpa.findById(idUser).orElseThrow(() ->
                new EntityNotFoundException("Пользователя не существует"));
        ItemRequest itemRequest = requestRepositoryJpa.findById(requestId).orElseThrow(() ->
                new EntityNotFoundException("Пользователя не существует"));
        Collection<Item> items = itemRepositoryJpa.findAllByRequestId(itemRequest.getId());
        return RequestMapper.toRequestDtoWithFeedbackItem(itemRequest, ItemMapper.mapToItemDto(items));
    }

    public Set<RequestDtoWithFeedbackItem> getRequestUserAll(int idUser) {
        User user = userRepositoryJpa.findById(idUser).orElseThrow(() ->
                new EntityNotFoundException("Пользователя не существует"));
        Set<RequestDtoWithFeedbackItem> result = new HashSet<>();
        Collection<ItemRequest> set = requestRepositoryJpa.findAllByRequesterId(idUser);
        Map<Integer, List<ItemDto>> itemsByRequest = addListItem(set);
        for (ItemRequest itemRequest : set) {
            result.add(RequestMapper.toRequestDtoWithFeedbackItem(itemRequest, (itemsByRequest.get(itemRequest.getId()))));
        }
        return result;
    }

    public Map<Integer, List<ItemDto>> addListItem(Collection<ItemRequest> set) {
        List<Integer> requestIds = set.stream()
                .map(ItemRequest::getId)
                .collect(toList());
        //вернуть все реквесты которые входят в лист реквестов этого юзера
        Map<Integer, List<ItemDto>> itemsByRequest = itemRepositoryJpa.findByRequestIdIn(requestIds)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(groupingBy(ItemDto::getRequestId, toList()));
        return itemsByRequest;
    }

    public List<RequestDtoWithFeedbackItem> getRequestAllPage(int idUser, Integer from, Integer size) {
        User user = userRepositoryJpa.findById(idUser).orElseThrow(() ->
                new EntityNotFoundException("Пользователя не существует"));
        List<RequestDtoWithFeedbackItem> result = new ArrayList<>();
        int page = from / size;
        List<ItemRequest> list = requestRepositoryJpa.findAllByRequesterIdNot(
                idUser, PageRequest.of(page, size, Sort.Direction.DESC, "created"));
        Map<Integer, List<ItemDto>> itemsByRequest = addListItem(list);
        for (ItemRequest itemRequest : list) {
            result.add(RequestMapper.toRequestDtoWithFeedbackItem(itemRequest, (itemsByRequest.get(itemRequest.getId()))));
        }
        return result;
    }
}




