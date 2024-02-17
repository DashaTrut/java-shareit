package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepositoryJpa;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepositoryJpa;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryJpaTest {

    @Autowired
    BookingRepositoryJpa bookingRepository;
    @Autowired
    private UserRepositoryJpa userRepository;
    @Autowired
    private ItemRepositoryJpa itemRepository;
    @Autowired
    private RequestRepositoryJpa requestRepository;

    private User user;
    private Item item;
    private ItemRequest itemRequest;
    private User user1;
    private User user2;
    private ItemRequest itemRequest1;
    private Booking bookingCurrent;

    private Booking bookingFuture;

    private Booking bookingPast;

    private Sort start = Sort.by(Sort.Direction.DESC, "start");
    int page = 0;
    int size = 1;
    Pageable pageable = PageRequest.of(page, size, start);


    @BeforeEach
    private void add() {
        LocalDateTime localDateTimeFuture = LocalDateTime.of(2024, 8, 4, 0, 0);
        LocalDateTime localDateTimeFuture1 = LocalDateTime.of(2024, 8, 7, 0, 0);
        LocalDateTime localDateTimePast = LocalDateTime.of(2023, 8, 4, 0, 0);
        LocalDateTime localDateTimePast1 = LocalDateTime.of(2023, 8, 7, 0, 0);
        LocalDateTime localDateTime = LocalDateTime.now();
        user = userRepository.save(User.builder()
                .email("test@email.com")
                .name("lasa")
                .build());

        itemRequest = requestRepository.save(ItemRequest.builder()
                .created(LocalDateTime.now())
                .description("test request")
                .requester(user)
                .build());
        user1 = userRepository.save(User.builder()
                .email("trut@email.com")
                .name("trut")
                .build());

        user2 = userRepository.save(User.builder()
                .email("t@email.com")
                .name("t")
                .build());

        itemRequest1 = requestRepository.save(ItemRequest.builder()
                .created(LocalDateTime.now())
                .description("test request two")
                .requester(user1)
                .build());

        item = itemRepository.save(Item.builder()
                .name("item")
                .owner(user)
                .available(true)
                .description("test item")
                .tags(Collections.EMPTY_SET)
                .build());

        bookingCurrent = bookingRepository.save(Booking.builder()
                .booker(user2)
                .start(localDateTime.minusHours(3))
                .end(localDateTime.plusHours(3))
                .item(item)
                .status(Status.APPROVED)
                .build());

        bookingFuture = bookingRepository.save(Booking.builder()
                .booker(user2)
                .start(localDateTimeFuture)
                .end(localDateTimeFuture1)
                .item(item)
                .status(Status.WAITING)
                .build());

        bookingPast = bookingRepository.save(Booking.builder()
                .booker(user2)
                .start(localDateTimePast)
                .end(localDateTimePast1)
                .item(item)
                .status(Status.APPROVED)
                .build());
    }


    @Test
    void findByBookerIdCurrent() {
        Collection<Booking> actualItemRequest = bookingRepository.findByBookerIdCurrent(user2.getId(), LocalDateTime.now(), start);
        assertTrue(!actualItemRequest.isEmpty());
        assertEquals(actualItemRequest.size(), 1);
        assertEquals(actualItemRequest, List.of(bookingCurrent));
    }

    @Test
    void testFindByBookerIdCurrent() {
        List<Booking> actualBooking = bookingRepository.findByBookerIdCurrent(user2.getId(), LocalDateTime.now(), pageable);
        assertTrue(!actualBooking.isEmpty());
        assertEquals(actualBooking.size(), 1);
        assertEquals(actualBooking, List.of(bookingCurrent));
    }

    @Test
    void findByBookerIdFuture() {
        Collection<Booking> actualBooking = bookingRepository.findByBookerIdFuture(user2.getId(), LocalDateTime.now(), start);
        assertTrue(!actualBooking.isEmpty());
        assertEquals(actualBooking.size(), 2);
        assertEquals(actualBooking, List.of(bookingFuture, bookingCurrent));
    }

    @Test
    void testFindByBookerIdFuture() {
        List<Booking> actualBooking = bookingRepository.findByBookerIdFuture(user2.getId(), LocalDateTime.now(), pageable);
        assertTrue(!actualBooking.isEmpty());
        assertEquals(actualBooking.size(), 1);
        assertEquals(actualBooking, List.of(bookingFuture));

    }

    @Test
    void findByItemOwnerOrderByStartDesc() {
        Collection<Booking> actualBooking = bookingRepository.findByItemOwnerOrderByStartDesc(user.getId(), start);
        assertTrue(!actualBooking.isEmpty());
        assertEquals(actualBooking.size(), 3);
        assertEquals(actualBooking, List.of(bookingFuture, bookingCurrent, bookingPast));

    }

    @Test
    void testFindByItemOwnerOrderByStartDesc() {
        List<Booking> actualBooking = bookingRepository.findByItemOwnerOrderByStartDesc(user.getId(), pageable);
        assertTrue(!actualBooking.isEmpty());
        assertEquals(actualBooking.size(), 1);
        assertEquals(actualBooking, List.of(bookingFuture));
    }


    @Test
    void findByItemOwnerFuture() {
        Collection<Booking> actualBooking = bookingRepository.findByItemOwnerFuture(user.getId(), LocalDateTime.now(), start);
        assertTrue(!actualBooking.isEmpty());
        assertEquals(actualBooking.size(), 2);
        assertEquals(actualBooking, List.of(bookingFuture, bookingCurrent));
    }

    @Test
    void testFindByItemOwnerFuture() {
        List<Booking> actualBooking = bookingRepository.findByItemOwnerFuture(user.getId(), LocalDateTime.now(), pageable);
        assertTrue(!actualBooking.isEmpty());
        assertEquals(actualBooking.size(), 1);
        assertEquals(actualBooking, List.of(bookingFuture));
    }

    @Test
    void findByItemOwnerPaste() {
        Collection<Booking> actualBooking = bookingRepository.findByItemOwnerPaste(user.getId(), LocalDateTime.now(), start);
        assertTrue(!actualBooking.isEmpty());
        assertEquals(actualBooking.size(), 1);
        assertEquals(actualBooking, List.of(bookingPast));
    }

    @Test
    void testFindByItemOwnerPaste() {
        List<Booking> actualBooking = bookingRepository.findByItemOwnerPaste(user.getId(), LocalDateTime.now(), pageable);
        assertTrue(!actualBooking.isEmpty());
        assertEquals(actualBooking.size(), 1);
        assertEquals(actualBooking, List.of(bookingPast));
    }

    @Test
    void findByItemOwnerCurrent() {
        Collection<Booking> actualBooking = bookingRepository.findByItemOwnerCurrent(user.getId(), LocalDateTime.now(), start);
        assertTrue(!actualBooking.isEmpty());
        assertEquals(actualBooking.size(), 1);
        assertEquals(actualBooking, List.of(bookingCurrent));
    }

    @Test
    void testFindByItemOwnerCurrent() {
        List<Booking> actualBooking = bookingRepository.findByItemOwnerCurrent(user.getId(), LocalDateTime.now(), pageable);
        assertTrue(!actualBooking.isEmpty());
        assertEquals(actualBooking.size(), 1);
        assertEquals(actualBooking, List.of(bookingCurrent));
    }


}