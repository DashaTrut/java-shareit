package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.*;

@UtilityClass
public class RequestMapper {

    public ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User user) {
        return ItemRequest.builder()
                .description(itemRequestDto.getDescription())
                .created(itemRequestDto.getCreated())
                .requester(user)
                .build();
    }

    public RequestResponseDto toRequestResponseDto(ItemRequest itemRequest) {
        return RequestResponseDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();
    }

    public RequestDtoWithFeedbackItem toRequestDtoWithFeedbackItem(ItemRequest itemRequest, List<ItemDto> list) {
        return RequestDtoWithFeedbackItem.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(list)
                .build();
    }

    public List<RequestResponseDto> toListRequestResponseDto(List<ItemRequest> itemRequest) {
        List<RequestResponseDto> result = new ArrayList<>();
        for (ItemRequest request : itemRequest) {
            result.add(toRequestResponseDto(request));
        }
        return result;
    }
}
