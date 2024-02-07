package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Component
public class ItemRepository {

    private final Map<Integer, Item> storageItem = new HashMap<>();
    private int generationId = 1;

    public Item create(Item item) {
        item.setId(generationId++);
        storageItem.put(item.getId(), item);
        return item;
    }


    public Item update(Item item) {
        if (!storageItem.containsKey(item.getId())) {
            throw new EntityNotFoundException(String.format("Обновление невозможно %s не сущесвует", item));
        }
        storageItem.put(item.getId(), item);
        return item;
    }


    public Item getId(int id) {
        if (storageItem.containsKey(id)) {
            return storageItem.get(id);
        }
        throw new EntityNotFoundException("Нет вещи с таким id");
    }

    public Collection<ItemDto> getItemsForUser(int id) {
        List<ItemDto> list = new ArrayList<>();
        for (Item item : storageItem.values()) {
            if (item.getOwner().getId() == id) {
                list.add(ItemMapper.toItemDto(item));
            }
        }
        return list;
    }


    public void delete(Item item) {
        if (storageItem.containsKey(item.getId())) {
            storageItem.remove(item.getId());
            return;
        }
        throw new EntityNotFoundException(String.format("Удаление невозможно %s не сущесвует", item));
    }

    public List<ItemDto> getItemsBySearch(String textQuery) {
        List<ItemDto> items = new ArrayList<>();
        for (Item item : storageItem.values()) {
            if ((item.getName().toLowerCase().contains(textQuery)
                    || item.getDescription().toLowerCase().contains(textQuery))
                    && item.getAvailable()) {
                items.add(ItemMapper.toItemDto(item));
            }
        }
        return items;
    }
}


