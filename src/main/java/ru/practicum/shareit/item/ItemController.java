package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private ItemServiceImpl itemServiceImpl;

    @PostMapping //добавление вещи;
    public Item addItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Integer id) {
        Item item = itemServiceImpl.add(itemDto, id);
        log.info("Add Item{}", item);
        return item;
    }

    @PatchMapping("{itemId}")
    public Item updateItem(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int id, @PathVariable int itemId) {
        Item item = itemServiceImpl.update(itemDto, id, itemId);
        log.info("Update Item{}", item);
        return item;
    }

    @GetMapping("{itemId}")
    public Item getForId(@PathVariable int itemId) {
        log.info("Get item id{}", itemId);
        return itemServiceImpl.getForId(itemId);
    }

    @GetMapping
    public Collection<Item> getItemsForUser(@RequestHeader("X-Sharer-User-Id") int id) {
        log.info("Get items user id{}", id);
        return itemServiceImpl.getItemsForUser(id);
    }

    @GetMapping("/search")
    public List<Item> searchItems(@RequestParam String text) {
        log.info("search {}, text");
        return itemServiceImpl.searchItem(text);
    }
}
