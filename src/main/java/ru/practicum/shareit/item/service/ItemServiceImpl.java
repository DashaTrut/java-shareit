package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepositoryJpa;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepositoryJpa;
import ru.practicum.shareit.item.repository.ItemRepositoryJpa;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepositoryJpa itemRepositoryJpa;
    private final UserRepositoryJpa userRepositoryJpa;
    private final BookingRepositoryJpa bookingRepositoryJpa;
    private final CommentRepositoryJpa commentRepositoryJpa;

    //добавление вещи;
    @Transactional(readOnly = true)
    public Item add(ItemDto itemDto, int id) {
        User user = userRepositoryJpa.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Пользователя не существует"));
        Item itemCreate = itemRepositoryJpa.save(ItemMapper.toItem(itemDto, user));
        return itemCreate;
    }

    @Transactional
    public ItemDto update(ItemDto itemDto, int id, int itemId) {
        Item item = getForId(itemId);
        if (item.getOwner().getId() == id) {
            if (itemDto.getName() != null) {
                item.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null) {
                item.setDescription(itemDto.getDescription());
            }
            if (itemDto.getAvailable() != null) {
                item.setAvailable(itemDto.getAvailable());
            }
            return ItemMapper.toItemDto(itemRepositoryJpa.save(item));
        } else {
            throw new EntityNotFoundException("Не совпадает id владельца вещи");
        }
    }

    @Transactional
    public Item getForId(int itemId) {
        return itemRepositoryJpa.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException("Вещи не существует"));
    }


    public ItemDtoBooking getForIdWithBooking(int itemId, int userId) {
        Item item = itemRepositoryJpa.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException("Вещи не существует"));
        User user = userRepositoryJpa.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("Пользователя не существует"));
        Set<Comment> comments = new HashSet<>(commentRepositoryJpa.findAllByItemOwnerId(item.getOwner().getId()));
        if (item.getOwner().getId() == userId) {
            Booking bookingLast = bookingRepositoryJpa.findFirstByItemIdAndStartBefore(itemId, LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start"));
            Booking bookingNext = bookingRepositoryJpa.findFirstByItemIdAndStartAfterAndStatus(item.getId(),
                    LocalDateTime.now(), Status.APPROVED, Sort.by(Sort.Direction.ASC, "start"));
            return ItemMapper.toItemDtoBooking(item, bookingLast, bookingNext, comments);
        }
        return ItemMapper.toItemDtoBooking(item, null, null, comments);
    }

    @Transactional
    public Collection<Item> getItemsForUser(Integer id) {
        return itemRepositoryJpa.findByOwnerId(id);
    }

    @Transactional
    public Collection<ItemDtoBooking> getItemsForUserWithBooking(Integer id) {
        User user = userRepositoryJpa.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Пользователя не существует"));
        Collection<Item> list = itemRepositoryJpa.findByOwnerId(id);
        List<ItemDtoBooking> listNew = new ArrayList<>();
        for (Item item : list) {
            Booking bookingLast = bookingRepositoryJpa.findFirstByItemIdAndEndBefore(item.getId(), LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start"));
            Booking bookingNext = bookingRepositoryJpa.findFirstByItemIdAndStartAfter(item.getId(), LocalDateTime.now(), Sort.by(Sort.Direction.ASC, "start"));
            Set<Comment> comments = new HashSet<>(commentRepositoryJpa.findAllByItemOwnerId(item.getOwner().getId()));
            listNew.add(ItemMapper.toItemDtoBooking(item, bookingLast, bookingNext, comments));
        }
        return listNew;
    }

    @Transactional
    public Collection<Item> getAllItem() {
        return itemRepositoryJpa.findAll();
    }

    //не знаю какой из двух правильнее оставить, работают оба
    public List<ItemDto> searchItem1(String textQuery) {
        if (textQuery == null || textQuery.isBlank()) {
            List<ItemDto> list = new ArrayList<>();
            return list;
        }
        List<ItemDto> items = new ArrayList<>();
        for (Item item : getAllItem()) {
            if ((item.getName().toLowerCase().contains(textQuery)
                    || item.getDescription().toLowerCase().contains(textQuery.toLowerCase()))
                    && item.getAvailable()) {
                items.add(ItemMapper.toItemDto(item));
            }
        }
        return items;
    }

    public List<ItemDto> searchItem(String textQuery) {
        if (textQuery == null || textQuery.isBlank()) {
            return Collections.EMPTY_LIST;
        }
        return ItemMapper.mapToItemDto(itemRepositoryJpa.search(textQuery));
    }

    @Transactional(readOnly = true)
    public CommentForItem addComment(CommentDto text, Integer itemId, Integer id) {
        Item item = itemRepositoryJpa.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException("Вещи не существует"));
        User user = userRepositoryJpa.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Пользователя не существует"));
        Booking booking = bookingRepositoryJpa.findFirstByBookerIdAndItemIdAndEndBefore(id, itemId, LocalDateTime.now()).orElseThrow(() ->
                new BookingException("Отзыв можно оставить только после бронирования"));

        Comment comment = commentRepositoryJpa.save(CommentMapper.toComment(text, user, item));
        return CommentMapper.toCommentForItem(comment);
    }

}
