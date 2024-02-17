package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingRepositoryJpa;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepositoryJpa;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepositoryJpa;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.booking.model.Status.WAITING;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepositoryJpa bookingRepository;

    @Mock
    private ItemRepositoryJpa itemRepositoryJpa;

    @Mock
    private UserRepositoryJpa userRepositoryJpa;

    @InjectMocks
    private BookingServiceImpl bookingService;


    @Test
    public void testGetBooking() {
        Booking booking = new Booking();
        booking.setId(1);
        User user = new User();
        user.setId(1);
        booking.setBooker(user);
        booking.setItem(new Item());
        when(bookingRepository.findById(anyInt())).thenReturn(Optional.of(booking));

        Booking booking1 = assertDoesNotThrow(() -> bookingService.getBooking(1, 1));
        assertEquals(booking1, booking);
    }

    @Test
    public void testGetBooking_isThrow() {
        Booking booking = new Booking();
        booking.setId(1);
        User user = new User();
        user.setId(1);
        booking.setBooker(user);
        booking.setItem(new Item());
        when(bookingRepository.findById(10)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingService.getBooking(10, 1));
    }

    @Test
    public void testUpdateStatusBooking_isSave() {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime localDateTime1 = LocalDateTime.now().plusHours(1);
        BookingDto bookingDto = new BookingDto(10, localDateTime, localDateTime1);

        User user = new User(1, "test@email.com", "lasa");
        Item item = new Item(10, "table", user, "big table", true, null, new HashSet<>());
        User user2 = new User(2, "testTestov@email.com", "nosa");
        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item, user2, Status.WAITING);
        Booking bookingUpdate = new Booking(1, localDateTime, localDateTime1, item, user2, Status.APPROVED);

        when(userRepositoryJpa.findById(1)).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.findById(10)).thenReturn(Optional.of(item));
        when(bookingRepository.findById(1)).thenReturn(Optional.of(bookingNew));
        when(bookingRepository.save(bookingUpdate)).thenReturn(bookingNew);

        Booking booking = bookingService.updateStatusBooking(1, true, 1);
        assertNotNull(booking);
        assertEquals(bookingUpdate, booking);
        assertEquals(bookingUpdate.getStatus(), booking.getStatus());
    }

    @Test
    public void testUpdateStatusBooking_isThrows() {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime localDateTime1 = LocalDateTime.now().plusHours(1);
        BookingDto bookingDto = new BookingDto(10, localDateTime, localDateTime1);

        User user = new User(1, "test@email.com", "lasa");
        Item item = new Item(10, "table", user, "big table", true, null, new HashSet<>());
        User user2 = new User(2, "testTestov@email.com", "nosa");
        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item, user2, Status.APPROVED);

        when(bookingRepository.findById(1)).thenReturn(Optional.of(bookingNew));

        assertThrows(BookingException.class, () -> bookingService.updateStatusBooking(1, true, 1));
    }


    @Test
    public void testCreateBooking_isSave() {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime localDateTime1 = LocalDateTime.now().plusHours(1);
        BookingDto bookingDto = new BookingDto(10, localDateTime, localDateTime1);

        User user = new User(1, "test@email.com", "lasa");
        Item item = new Item(10, "table", user, "big table", true, null, new HashSet<>());
        User user2 = new User(2, "testTestov@email.com", "nosa");
        Booking bookingNew = new Booking(0, localDateTime, localDateTime1, item, user, Status.WAITING);

        when(userRepositoryJpa.findById(2)).thenReturn(Optional.of(user2));
        when(itemRepositoryJpa.findById(10)).thenReturn(Optional.of(item));
        when(bookingRepository.save(bookingNew)).thenReturn(bookingNew);

        Booking booking = bookingService.create(bookingDto, 2);
        assertNotNull(booking);
        assertEquals(item, booking.getItem());
        assertEquals(bookingNew.getItem(), booking.getItem());

    }

    @Test
    public void testCreateBooking_toHimself_withThrown() {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime localDateTime1 = LocalDateTime.now().plusHours(1);
        BookingDto bookingDto = new BookingDto(10, localDateTime, localDateTime1);

        User user = new User(1, "test@email.com", "lasa");
        Item item = new Item(10, "table", user, "big table", true, null, new HashSet<>());
        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item, user, Status.WAITING);

        when(userRepositoryJpa.findById(1)).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.findById(10)).thenReturn(Optional.of(item));

        assertThrows(EntityNotFoundException.class, () -> bookingService.create(bookingDto, 1));

    }

    @Test
    public void testGetBookingForState_isGetWaitingNotPage() {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime localDateTime1 = LocalDateTime.now().plusHours(1);

        User user = new User(1, "test@email.com", "lasa");
        Item item = new Item(10, "table", user, "big table", true, null, new HashSet<>());
        User user2 = new User(2, "testTestov@email.com", "nosa");
        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item, user, WAITING);
        //второй запрашивает
        List<Booking> returnList = Arrays.asList(bookingNew);
        when(userRepositoryJpa.findById(2)).thenReturn(Optional.of(user2));

        Collection<Booking> bookings = bookingService.getBookingForState(2, "WAITING", null, null);
        assertNotNull(bookings);
        assertEquals(bookings, bookings);
    }

    @Test
    public void testGetBookingForState_isThrow() {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime localDateTime1 = LocalDateTime.now().plusHours(1);

        User user = new User(1, "test@email.com", "lasa");
        Item item = new Item(10, "table", user, "big table", true, null, new HashSet<>());
        User user2 = new User(2, "testTestov@email.com", "nosa");
        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item, user, WAITING);
        //второй запрашивает
        List<Booking> returnList = Arrays.asList(bookingNew);
        when(userRepositoryJpa.findById(2)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> bookingService.getBookingForState(2, "WAITING", null, null));

    }

    @Test
    public void testGetBookingForState_isGetWaitingIsPage() {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime localDateTime1 = LocalDateTime.now().plusHours(1);
        BookingDto bookingDto = new BookingDto(10, localDateTime, localDateTime1);

        User user = new User(1, "test@email.com", "lasa");
        Item item = new Item(10, "table", user, "big table", true, null, new HashSet<>());
        User user2 = new User(2, "testTestov@email.com", "nosa");
        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item, user, WAITING);
        //второй запрашивает
        List<Booking> returnList = Arrays.asList(bookingNew);
        when(userRepositoryJpa.findById(2)).thenReturn(Optional.of(user2));

        Collection<Booking> bookings = bookingService.getBookingForState(2, "WAITING", 1, 1);
        assertNotNull(bookings);
        assertEquals(bookings, bookings);
    }

    @Test
    public void testGetBookingForState_isThrowPage() {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime localDateTime1 = LocalDateTime.now().plusHours(1);
        BookingDto bookingDto = new BookingDto(10, localDateTime, localDateTime1);

        User user = new User(1, "test@email.com", "lasa");
        Item item = new Item(10, "table", user, "big table", true, null, new HashSet<>());
        User user2 = new User(2, "testTestov@email.com", "nosa");
        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item, user, WAITING);
        //второй запрашивает
        List<Booking> returnList = Arrays.asList(bookingNew);
        when(userRepositoryJpa.findById(2)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> bookingService.getBookingForState(2, "WAITING", 1, 1));
    }

    @Test
    public void testGetBookingForState_isThrowValidPage() {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime localDateTime1 = LocalDateTime.now().plusHours(1);
        BookingDto bookingDto = new BookingDto(10, localDateTime, localDateTime1);

        User user = new User(1, "test@email.com", "lasa");
        Item item = new Item(10, "table", user, "big table", true, null, new HashSet<>());
        User user2 = new User(2, "testTestov@email.com", "nosa");
        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item, user, WAITING);
        //второй запрашивает
        List<Booking> returnList = Arrays.asList(bookingNew);
        when(userRepositoryJpa.findById(2)).thenReturn(Optional.of(user2));

        assertThrows(IllegalArgumentException.class, () -> bookingService.getBookingForState(2, "WAITING", -2, 1));
    }

    @Test
    public void testGetBookingForOwnerAndState_isFutureNotPage() {
        LocalDateTime localDateTime = LocalDateTime.now().plusHours(1);
        LocalDateTime localDateTime1 = LocalDateTime.now().plusHours(10);

        User user = new User(1, "test@email.com", "lasa");
        User user2 = new User(2, "testTestov@email.com", "nosa");
        Item item = new Item(10, "table", user2, "big table", true, null, new HashSet<>());

        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item, user, WAITING);
        //второй запрашивает
        List<Booking> returnList = Arrays.asList(bookingNew);
        when(userRepositoryJpa.findById(2)).thenReturn(Optional.of(user2));
        when(itemRepositoryJpa.findByOwnerId(2)).thenReturn(Arrays.asList(new Item()));

        Collection<Booking> bookings = bookingService.getBookingForOwnerAndState(2, "FUTURE", null, null);
        assertNotNull(bookings);
        assertEquals(bookings, bookings);
    }

    @Test
    public void testGetBookingForOwnerAndState_isThrowFutureNotPage() {
        LocalDateTime localDateTime = LocalDateTime.now().plusHours(1);
        LocalDateTime localDateTime1 = LocalDateTime.now().plusHours(10);

        User user = new User(1, "test@email.com", "lasa");
        User user2 = new User(2, "testTestov@email.com", "nosa");
        Item item = new Item(10, "table", user2, "big table", true, null, new HashSet<>());

        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item, user, WAITING);
        //второй запрашивает
        List<Booking> returnList = Arrays.asList(bookingNew);
        when(userRepositoryJpa.findById(2)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> bookingService.getBookingForOwnerAndState(2, "FUTURE", null, null));
    }

    @Test
    public void testGetBookingForOwnerAndState_isPastWithPage() {
        LocalDateTime localDateTime = LocalDateTime.now().minusHours((10));
        LocalDateTime localDateTime1 = LocalDateTime.now().minusHours((3));

        User user = new User(1, "test@email.com", "lasa");
        User user2 = new User(2, "testTestov@email.com", "nosa");
        Item item = new Item(10, "table", user2, "big table", true, null, new HashSet<>());

        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item, user, WAITING);
        //второй запрашивает
        List<Booking> returnList = Arrays.asList(bookingNew);
        when(userRepositoryJpa.findById(2)).thenReturn(Optional.of(user2));
        when(itemRepositoryJpa.findByOwnerId(2)).thenReturn(Arrays.asList(new Item()));

        Collection<Booking> bookings = bookingService.getBookingForOwnerAndState(2, "PAST", 1, 1);
        assertNotNull(bookings);
        assertEquals(bookings, bookings);
    }

    @Test
    public void testGetBookingForState_isThrowWaitingNotPage() {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime localDateTime1 = LocalDateTime.now().plusHours(1);
        BookingDto bookingDto = new BookingDto(10, localDateTime, localDateTime1);

        User user = new User(1, "test@email.com", "lasa");
        Item item = new Item(10, "table", user, "big table", true, null, new HashSet<>());
        User user2 = new User(2, "testTestov@email.com", "nosa");
        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item, user, WAITING);
        //второй запрашивает
        List<Booking> returnList = Arrays.asList(bookingNew);

        assertThrows(ValidationException.class, () -> bookingService.getBookingForState(2, "Test", null, null));
    }

}