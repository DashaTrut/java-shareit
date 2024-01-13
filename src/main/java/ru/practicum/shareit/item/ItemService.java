package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;


import java.util.Collection;
import java.util.List;

public interface ItemService {
    public Item add(ItemDto itemDto, Integer id);


    public Item update(ItemDto itemDto, int id, int itemId);


    public Item getForId(int itemId);

    public Collection<Item> getItemsForUser(int id);


    public List<Item> searchItem(String textQuery);
}
