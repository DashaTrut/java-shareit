package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;


    @PostMapping //добавление вещи;
    public ResponseEntity<Object> addItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Integer id) {
        log.info("Add Item{}", itemDto);
        return itemClient.add(itemDto, id);
    }

    @PatchMapping("{itemId}")
    public ResponseEntity<Object> updateItem(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int id, @PathVariable int itemId) {
        log.info("Update Item{}", itemDto);
        return itemClient.update(itemDto, id, itemId);
    }

    @GetMapping("{itemId}")
    public ResponseEntity<Object> getForId(@RequestHeader("X-Sharer-User-Id") int id, @PathVariable int itemId) {
        log.info("Get item id{}", itemId);
        return itemClient.getForIdWithBooking(id, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsForUser(@RequestHeader("X-Sharer-User-Id") int id,
                                                  @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
                                                  @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {
        log.info("Get items user id{}", id);
        return itemClient.getItemsForUserWithBooking(id, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text,
                                              @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
                                              @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {
        log.info("search {}, text");
        return itemClient.searchItem(text, from, size);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestBody @Valid CommentDto text, @PathVariable int itemId, @RequestHeader("X-Sharer-User-Id") Integer id) {
        return itemClient.addComment(text, itemId, id);
    }
}
