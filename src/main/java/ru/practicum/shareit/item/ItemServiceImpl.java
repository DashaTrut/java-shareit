package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    //добавление вещи;
    public Item add(ItemDto itemDto, Integer id) {
        if (id != null) {
            User user = userRepository.getUser(id);
            Item item = itemRepository.create(Item.builder()
                    .name(itemDto.getName())
                    .description(itemDto.getDescription())
                    .available(itemDto.getAvailable())
                    .request(null)
                    .build());
            item.setOwner(user);
            return item;
        } else {
            throw new EntityNotFoundException("не передали id");
        }
    }

    public Item update(ItemDto itemDto, int id, int itemId) {
        Item item = Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .id(itemId)
                .available(itemDto.getAvailable())
                .request(itemDto.getRequest())
                .build();
        item.setOwner(userRepository.getUser(id));
        Item oldItem = getForId(itemId);
        if (oldItem.getOwner().getId() == id) {
            if (item.getName() == null) {
                item.setName(oldItem.getName());
            }
            if (item.getDescription() == null) {
                item.setDescription(oldItem.getDescription());
            }
            if (item.getAvailable() == null) {
                item.setAvailable(oldItem.getAvailable());
            }
            item.setId(itemId);
            return itemRepository.update(item, id);
        } else {
            throw new EntityNotFoundException("Не совпадает id владельца вещи");
        }
    }

    public Item getForId(int itemId) {
        return itemRepository.getId(itemId);
    }

    public Collection<Item> getItemsForUser(int id) {
        return itemRepository.getItemsForUser(id);
    }


    public List<Item> searchItem(String textQuery) {
        if (textQuery.isBlank()) {
            List<Item> list = new ArrayList<>();
            return list;
        }
        return itemRepository.getItemsBySearch(textQuery.toLowerCase());
    }
}
