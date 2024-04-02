package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentForItem;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @MockBean
    private ItemServiceImpl itemService;

    public static final String HEADER_USER = "X-Sharer-User-Id";
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    @Test
    void getForIdWithBooking() {
        int itemId = 1;
        int userId = 1;
        ItemDtoBooking itemDtoBooking = new ItemDtoBooking(itemId, "item for test", "check true", true, null, null, null, Collections.EMPTY_SET);
        when(itemService.getForIdWithBooking(anyInt(), anyInt())).thenReturn(itemDtoBooking);
        mockMvc.perform(get("/items/{itemId}", itemId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER, userId))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void addItem() {
        int itemId = 1;
        int userId = 1;

        User user = new User(userId, "test@email.com", "lasa");
        Item item = new Item(itemId, "item for test", user, "check true", true, null, Collections.EMPTY_SET);
        ItemDto itemDto = new ItemDto(itemId, "item for test", "check true", true, null);
        when(itemService.add(any(ItemDto.class), anyInt())).thenReturn(item);
        mockMvc.perform(post("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto))
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER, userId))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void updateItem() {
        int itemId = 1;
        int userId = 1;
        ItemDto itemDto = new ItemDto(itemId, "item for test", "check true", true, null);
        when(itemService.update(any(ItemDto.class), anyInt(), anyInt())).thenReturn(itemDto);
        mockMvc.perform(patch("/items/{itemId}", itemId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto))
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER, userId))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void getItemsForUser() {
        int itemId = 1;
        int userId = 1;
        ItemDtoBooking itemDtoBooking = new ItemDtoBooking(itemId, "item for test", "check true", true, null, null, null, Collections.EMPTY_SET);

        when(itemService.getItemsForUserWithBooking(anyInt(), anyInt(), anyInt())).thenReturn(List.of(itemDtoBooking));
        mockMvc.perform(get("/items/{itemId}", itemId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER, userId))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void searchItems() {
        int itemId = 1;
        int userId = 1;
        String text = "check";
        ItemDto itemDto = new ItemDto(itemId, "item for test", "check true", true, null);

        when(itemService.searchItem(anyString(), anyInt(), anyInt())).thenReturn(List.of(itemDto));
        mockMvc.perform(get("/items/search")
                        .param("text", text)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER, userId))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void addComment() {
        int itemId = 1;
        int userId = 1;
        User user = new User(userId, "test@email.com", "lasa");

        CommentDto text = new CommentDto("bad item");
        CommentForItem comment = new CommentForItem(1, "bad item", userId, user.getName(), itemId, LocalDateTime.now());
        when(itemService.addComment(any(CommentDto.class), anyInt(), anyInt())).thenReturn(comment);
        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(text))
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER, userId))
                .andExpect(status().isOk());
    }
}