package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemServiceImpl itemServiceImpl;


    @PostMapping //добавление вещи;
    public ItemDto addItem(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Integer id) {
        Item item = itemServiceImpl.add(itemDto, id);
        log.info("Add Item{}", item);
        return ItemMapper.toItemDto(item);
    }

    @PatchMapping("{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int id, @PathVariable int itemId) {
        ItemDto item = itemServiceImpl.update(itemDto, id, itemId);
        log.info("Update Item{}", item);
        return item;
    }

    @GetMapping("{itemId}")
    public ItemDtoBooking getForId(@RequestHeader("X-Sharer-User-Id") int id, @PathVariable int itemId) {
        log.info("Get item id{}", itemId);
        return itemServiceImpl.getForIdWithBooking(itemId, id);
    }

    @GetMapping
    public Collection<ItemDtoBooking> getItemsForUser(@RequestHeader("X-Sharer-User-Id") int id,
                                                      @RequestParam(defaultValue = "0", required = false) Integer from,
                                                      @RequestParam(defaultValue = "10", required = false) Integer size) {
        log.info("Get items user id{}", id);
        return itemServiceImpl.getItemsForUserWithBooking(id, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text,
                                     @RequestParam(defaultValue = "0", required = false) Integer from,
                                     @RequestParam(defaultValue = "10", required = false) Integer size) {
        log.info("search {}, text");
        return itemServiceImpl.searchItem(text, from, size);
    }

    @PostMapping("{itemId}/comment")
    public CommentForItem addComment(@RequestBody CommentDto text, @PathVariable int itemId, @RequestHeader("X-Sharer-User-Id") Integer id) {
        return itemServiceImpl.addComment(text, itemId, id);
    }
}
