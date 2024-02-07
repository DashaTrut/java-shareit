package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    ItemDto add(ItemDto itemDto, Integer id);


    ItemDto update(ItemDto itemDto, int id, int itemId);


    ItemDto getForId(int itemId);

    Collection<ItemDto> getItemsForUser(int id);


    List<ItemDto> searchItem(String textQuery);
}
