package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Component
public class ItemRepository {

    private final Map<Integer, Item> storageItem = new HashMap<>();
    private int generationId = 0;

    public Item create(Item item) {
        ++generationId;
        item.setId(generationId);
        storageItem.put(item.getId(), item);
        return item;
    }


    public Item update(Item item, int idUser) {
        if (!storageItem.containsKey(item.getId())) {
            throw new EntityNotFoundException(String.format("Обновление невозможно %s не сущесвует", item));
        }
        int idOldUser = storageItem.get(item.getId()).getOwner().getId();
        if (idOldUser != idUser) {
            throw new EntityNotFoundException("Не совпадает id владельца вещи");
        }
        storageItem.put(item.getId(), item);
        return item;
    }

    public Collection<Item> getAll() {
        ArrayList<Item> list = new ArrayList<>();
        if (storageItem.isEmpty()) {
            return list;
        }
        for (Item item : storageItem.values()) {
            list.add(item);
        }
        return list;
    }


    public Item getId(int id) {
        if (storageItem.containsKey(id)) {
            return storageItem.get(id);
        }
        throw new EntityNotFoundException("Нет вещи с таким id");
    }

    public Collection<Item> getItemsForUser(int id) {
        List<Item> list = new ArrayList<>();
        for (Item item : storageItem.values()) {
            if (item.getOwner().getId() == id) {
                list.add(item);
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

    public List<Item> getItemsBySearch(String textQuery) {
        List<Item> items = new ArrayList<>();
        for (Item item : storageItem.values()) {
            if ((item.getName().toLowerCase().contains(textQuery)
                    || item.getDescription().toLowerCase().contains(textQuery))
                    && item.getAvailable()) {
                items.add(item);
            }
        }
        return items;
    }
}


