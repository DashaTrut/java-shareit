package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@Valid @RequestBody ItemRequestDto itemRequestDto, @RequestHeader("X-Sharer-User-Id") Integer id) {
        log.info("Add Request{}", itemRequestDto);
        return itemRequestClient.create(itemRequestDto, id);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestUserAll(@RequestHeader("X-Sharer-User-Id") Integer idUser) {
        return itemRequestClient.getRequestUserAll(idUser);
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> getRequest(@PathVariable Integer requestId, @RequestHeader("X-Sharer-User-Id") Integer idUser) {
        return itemRequestClient.getRequest(requestId, idUser);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getRequestAllPage(@RequestHeader("X-Sharer-User-Id") int idUser,
                                                    @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
                                                    @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {
        return itemRequestClient.getRequestAllPage(idUser, from, size);
    }
}
