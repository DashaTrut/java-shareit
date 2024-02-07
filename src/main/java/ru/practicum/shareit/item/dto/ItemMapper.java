package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@UtilityClass
public class ItemMapper {

    public ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest() : null);
    }


    public Item toItem(ItemDto itemDto, User user) {
        return Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .owner(user)
                .available(itemDto.getAvailable())
                .request(null)
                .build();
    }

    public List<ItemDto> mapToItemDto(Iterable<Item> items) {
        List<ItemDto> result = new ArrayList<>();
        for (Item item : items) {
            result.add(toItemDto(item));
        }
        return result;
    }

    public ItemDtoBooking toItemDtoBooking(Item item, Booking lastBooking, Booking nextBooking, Set<Comment> set) {
        BookingDtoForItem lastDto;
        BookingDtoForItem nextDto;
        if (lastBooking != null) {
            lastDto = BookingMapper.toBookingDtoForItem(lastBooking);
        } else {
            lastDto = null;
        }
        if (nextBooking != null) {
            nextDto = BookingMapper.toBookingDtoForItem(nextBooking);
        } else {
            nextDto = null;
        }
        return new ItemDtoBooking(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest() : null,
                lastDto,
                nextDto,
                CommentMapper.toSetCommentForItem(set));
    }

}
