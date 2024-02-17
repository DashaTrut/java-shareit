package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    Item add(ItemDto itemDto, int id);


    ItemDto update(ItemDto itemDto, int id, int itemId);


    Item getForId(int itemId);

    Collection<Item> getItemsForUser(Integer id);


    List<ItemDto> searchItem(String textQuery);
}
