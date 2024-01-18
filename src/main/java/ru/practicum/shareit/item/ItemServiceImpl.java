package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    //добавление вещи;
    public ItemDto add(ItemDto itemDto, Integer id) {
        if (id != null) {
            User user = userRepository.getUser(id);
            Item item = itemRepository.create(ItemMapper.toItem(itemDto, user));
            return ItemMapper.toItemDto(item);
        } else {
            throw new EntityNotFoundException("не передали id");
        }
    }

    public ItemDto update(ItemDto itemDto, int id, int itemId) {
        Item item = getForIdItem(itemId);
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
            return ItemMapper.toItemDto(itemRepository.update(item));
        } else {
            throw new EntityNotFoundException("Не совпадает id владельца вещи");
        }
    }

    public ItemDto getForId(int itemId) {
        return ItemMapper.toItemDto(itemRepository.getId(itemId));
    }

    public Item getForIdItem(int itemId) {
        return itemRepository.getId(itemId);
    }

    public Collection<ItemDto> getItemsForUser(int id) {
        return itemRepository.getItemsForUser(id);
    }


    public List<ItemDto> searchItem(String textQuery) {
        if (textQuery == null || textQuery.isBlank()) {
            List<ItemDto> list = new ArrayList<>();
            return list;
        }
        return itemRepository.getItemsBySearch(textQuery.toLowerCase());
    }
}