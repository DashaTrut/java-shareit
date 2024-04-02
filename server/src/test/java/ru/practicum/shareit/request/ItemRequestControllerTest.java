package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDtoWithFeedbackItem;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.booking.model.Status.APPROVED;
import static ru.practicum.shareit.booking.model.Status.WAITING;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    @MockBean
    private RequestService requestService;

    public static final String HEADER_USER = "X-Sharer-User-Id";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    private LocalDateTime localDateTime;
    private LocalDateTime localDateTimeDto;

    private LocalDateTime localDateTime1;
    private User user;
    private Item item;
    private Booking bookingNew;
    private Booking bookingApproved;
    private BookingDto dto;
    private ItemRequestDto itemRequestDto;
    private ItemRequest itemRequest;
    private RequestDtoWithFeedbackItem requestDtoWithFeedbackItem;
    private ItemDto itemDto;

    @BeforeEach
    void beforeEach() {
        localDateTime = LocalDateTime.of(2024, 8, 4, 0, 0);
        localDateTime1 = LocalDateTime.of(2024, 8, 7, 0, 0);
        localDateTimeDto = LocalDateTime.of(2023, 8, 4, 0, 0);
        itemRequestDto = new ItemRequestDto("i need item for test", localDateTimeDto);

        user = new User(1, "test@email.com", "lasa");
        item = new Item(1, "table", user, "big table", true, null, new HashSet<>());
        itemDto = new ItemDto(1, "table", "big table", true, 1);

        bookingNew = new Booking(1, localDateTime, localDateTime1, item, WAITING, user);
        bookingApproved = new Booking(1, localDateTime, localDateTime1, item, APPROVED, user);
        itemRequest = new ItemRequest(1, "i need item for test", user, localDateTimeDto);
        requestDtoWithFeedbackItem = new RequestDtoWithFeedbackItem(1, "i need item for test", localDateTimeDto, List.of(itemDto));
        dto = new BookingDto(1, localDateTime, localDateTime1);
    }

    @SneakyThrows
    @Test
    void createRequest() {
        int userId = 1;

        when(requestService.create(any(ItemRequestDto.class), anyInt())).thenReturn(itemRequest);
        mockMvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER, userId))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void getRequestUserAll() {
        int userId = 1;

        when(requestService.getRequestAllPage(anyInt(), anyInt(), anyInt())).thenReturn(List.of(requestDtoWithFeedbackItem));
        mockMvc.perform(get("/requests")
                        //.content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER, userId))
                .andExpect(status().isOk());
    }


    @SneakyThrows
    @Test
    void getRequest() {
        int userId = 1;
        int requestId = 1;
        when(requestService.getRequest(anyInt(), anyInt())).thenReturn(requestDtoWithFeedbackItem);
        mockMvc.perform(get("/requests/{requestId}", requestId)
                        //.content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER, userId))
                .andExpect(status().isOk());
        verify(requestService).getRequest(anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void getRequestAllPage() {
        int userId = 1;
        when(requestService.getRequestAllPage(anyInt(), anyInt(), anyInt())).thenReturn(List.of(requestDtoWithFeedbackItem));
        mockMvc.perform(get("/requests/all")
                        // .param("state", "ALL")
                        .param("from", String.valueOf(1))
                        .param("size", String.valueOf(10))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER, userId))
                .andExpect(status().isOk());
        verify(requestService).getRequestAllPage(anyInt(), anyInt(), anyInt());
    }
}