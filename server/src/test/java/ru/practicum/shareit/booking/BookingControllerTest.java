package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.booking.model.Status.APPROVED;
import static ru.practicum.shareit.booking.model.Status.WAITING;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @MockBean
    private BookingServiceImpl bookingService;

    public static final String HEADER_USER = "X-Sharer-User-Id";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    private LocalDateTime localDateTime;
    private LocalDateTime localDateTime1;
    private User user;
    private Item item;
    private Booking bookingNew;
    private Booking bookingApproved;
    private BookingDto dto;

    @BeforeEach
    void beforeEach() {
        localDateTime = LocalDateTime.of(2024, 8, 4, 0, 0);
        localDateTime1 = LocalDateTime.of(2024, 8, 7, 0, 0);

        user = new User(1, "test@email.com", "lasa");
        item = new Item(1, "table", user, "big table", true, null, new HashSet<>());
        bookingNew = new Booking(1, localDateTime, localDateTime1, item, WAITING, user);
        bookingApproved = new Booking(1, localDateTime, localDateTime1, item, APPROVED, user);

        dto = new BookingDto(1, localDateTime, localDateTime1);
    }


    @SneakyThrows
    @Test
    void addBooking() {
        int userId = 1;

        when(bookingService.create(any(BookingDto.class), anyInt())).thenReturn(bookingApproved);
        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER, userId))
                .andExpect(status().isOk());
    }


    @SneakyThrows
    @Test
    void updateStatusBooking() {
        int userId = 1;
        int bookingId = 1;
        Boolean approved = true;

        when(bookingService.updateStatusBooking(anyInt(), anyBoolean(), anyInt())).thenReturn(bookingNew);
        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER, userId))
                .andExpect(status().isOk());
    }


    @SneakyThrows
    @Test
    void getBooking() {
        int userId = 1;
        int bookingId = 1;
        when(bookingService.getBooking(anyInt(), anyInt())).thenReturn(bookingApproved);
        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER, userId))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void getBookingForState() {
        int userId = 1;
        when(bookingService.getBookingForState(anyInt(), anyString(), anyInt(), anyInt())).thenReturn(List.of(bookingApproved));
        mockMvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .param("from", String.valueOf(1))
                        .param("size", String.valueOf(10))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER, userId))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void getBookingForOwnerAndState() {
        int userId = 1;
        when(bookingService.getBookingForState(anyInt(), anyString(), anyInt(), anyInt())).thenReturn(List.of(bookingApproved));
        mockMvc.perform(get("/bookings/owner")
                        .param("state", "FUTURE")
                        .param("from", String.valueOf(1))
                        .param("size", String.valueOf(10))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER, userId))
                .andExpect(status().isOk());
    }
}
