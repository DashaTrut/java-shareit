package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemServiceImpl itemServiceImpl;


    @PostMapping //добавление вещи;
    public ItemDto addItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Integer id) {
        ItemDto item = itemServiceImpl.add(itemDto, id);
        log.info("Add Item{}", item);
        return item;
    }

    @PatchMapping("{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int id, @PathVariable int itemId) {
        ItemDto item = itemServiceImpl.update(itemDto, id, itemId);
        log.info("Update Item{}", item);
        return item;
    }

    @GetMapping("{itemId}")
    public ItemDto getForId(@PathVariable int itemId) {
        log.info("Get item id{}", itemId);
        ItemDto item = itemServiceImpl.getForId(itemId);
        return item;
    }

    @GetMapping
    public Collection<ItemDto> getItemsForUser(@RequestHeader("X-Sharer-User-Id") int id) {
        log.info("Get items user id{}", id);
        return itemServiceImpl.getItemsForUser(id);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        log.info("search {}, text");
        return itemServiceImpl.searchItem(text);
    }
}