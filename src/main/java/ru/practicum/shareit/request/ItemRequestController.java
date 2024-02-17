package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDtoWithFeedbackItem;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final RequestService requestService;

    @PostMapping
    public RequestResponseDto createRequest(@Valid @RequestBody ItemRequestDto itemRequestDto, @RequestHeader("X-Sharer-User-Id") Integer id) {
        log.info("Add Request{}", itemRequestDto);
        return RequestMapper.toRequestResponseDto(requestService.create(itemRequestDto, id));
    }

    @GetMapping
    public Set<RequestDtoWithFeedbackItem> getRequestUserAll(@RequestHeader("X-Sharer-User-Id") Integer idUser) {
        return requestService.getRequestUserAll(idUser);
    }

    @GetMapping("{requestId}")
    public RequestDtoWithFeedbackItem getRequest(@PathVariable Integer requestId, @RequestHeader("X-Sharer-User-Id") Integer idUser) {
        return requestService.getRequest(requestId, idUser);
    }

    @GetMapping("/all")
    public List<RequestDtoWithFeedbackItem> getRequestAllPage(@RequestHeader("X-Sharer-User-Id") int idUser,
                                                              @RequestParam(required = false) @Positive Integer from, @RequestParam(required = false) @NotNull @Positive Integer size) {
        return requestService.getRequestAllPage(idUser, from, size);
    }

}
