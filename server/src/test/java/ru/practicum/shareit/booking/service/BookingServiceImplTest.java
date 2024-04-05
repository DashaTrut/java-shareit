package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.BookingRepositoryJpa;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.BookingNotFoundException;
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
import static ru.practicum.shareit.booking.model.Status.REJECTED;
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

    private User user;
    private User user3;
    private User user2;
    private Item item;

    private LocalDateTime localDateTime;
    private LocalDateTime localDateTime1;
    private BookingDto bookingDto;
    private Item item10;
    private Item item9;


    @BeforeEach
    private void addItem() {

        localDateTime = LocalDateTime.now();
        localDateTime1 = LocalDateTime.now().plusHours(1);

        user = User.builder()
                .id(1)
                .email("test@email.com")
                .name("lasa")
                .build();


        user3 = User.builder()
                .id(3)
                .email("trut@email.com")
                .name("trut")
                .build();

        user2 = User.builder()
                .id(2)
                .email("t@email.com")
                .name("t")
                .build();


        item = Item.builder()
                .id(1)
                .name("item")
                .owner(user2)
                .available(true)
                .description("test item")
                .tags(Collections.EMPTY_SET)
                .build();

        bookingDto = new BookingDto(
                10, localDateTime, localDateTime1);

        item10 = new Item(
                10, "table", user, "big table", true,
                null, new HashSet<>());
        item9 = new Item(
                9, "table", user, "big table", false,
                null, new HashSet<>());
    }


    @Test
    public void testGetBooking() {
        Booking booking = new Booking();
        booking.setId(1);
        booking.setBooker(user);
        booking.setItem(item);
        when(bookingRepository.findById(anyInt())).thenReturn(Optional.of(booking));

        Booking booking1 = assertDoesNotThrow(() -> bookingService.getBooking(1, 1));
        assertEquals(booking1, booking);
    }

    @Test
    public void testGetBooking_isThrow() {
        Booking booking = new Booking();
        booking.setId(1);
        booking.setBooker(user);
        booking.setItem(item);
        when(bookingRepository.findById(10)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingService.getBooking(10, 1));
        verify(bookingRepository, times(1)).findById(anyInt());

    }

    @Test
    public void testGetBooking_isThrowBookingNotFoundException() {
        Booking booking = new Booking();
        booking.setId(1);
        booking.setBooker(user);
        booking.setItem(item);
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));

        assertThrows(BookingNotFoundException.class, () -> bookingService.getBooking(1, 4));
        verify(bookingRepository, times(1)).findById(anyInt());

    }

    @Test
    public void testGetBooking_isThrowTwoBookingNotFoundException() {
        Booking booking = new Booking();
        booking.setId(1);
        booking.setBooker(user);
        booking.setItem(item);
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));

        Booking booking1 = assertDoesNotThrow(() -> bookingService.getBooking(1, 2));
        assertEquals(booking1, booking);
    }

    @Test
    public void testUpdateStatusBooking_isSave() {
        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item10, Status.WAITING, user2);
        Booking bookingUpdate = new Booking(1, localDateTime, localDateTime1, item10, Status.APPROVED,  user2);

        when(userRepositoryJpa.findById(1)).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.findById(10)).thenReturn(Optional.of(item10));
        when(bookingRepository.findById(1)).thenReturn(Optional.of(bookingNew));
        when(bookingRepository.save(bookingUpdate)).thenReturn(bookingNew);

        Booking booking = bookingService.updateStatusBooking(1, true, 1);
        assertNotNull(booking);
        assertEquals(bookingUpdate, booking);
        assertEquals(bookingUpdate.getStatus(), booking.getStatus());
    }

    @Test
    public void testUpdateStatusBooking_isSaveFalse() {
        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item10, Status.WAITING, user2);
        Booking bookingUpdate = new Booking(1, localDateTime, localDateTime1, item10, Status.APPROVED, user2);

        when(userRepositoryJpa.findById(1)).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.findById(10)).thenReturn(Optional.of(item10));
        when(bookingRepository.findById(1)).thenReturn(Optional.of(bookingNew));
        when(bookingRepository.save(bookingUpdate)).thenReturn(bookingUpdate);

        Booking booking = bookingService.updateStatusBooking(1, false, 1);
        assertNotNull(booking);
        assertEquals(bookingUpdate, booking);
        assertEquals(bookingUpdate.getStatus(), booking.getStatus());
    }

    @Test
    public void testUpdateStatusBooking_isThrows() {
        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item10, Status.APPROVED, user2);

        when(bookingRepository.findById(1)).thenReturn(Optional.of(bookingNew));

        assertThrows(BookingException.class, () -> bookingService.updateStatusBooking(1, true, 1));
        verify(bookingRepository, never()).save(bookingNew);
        verify(bookingRepository, times(0)).save(bookingNew);
    }

    @Test
    public void testUpdateStatusBooking_isThrowsFalse() {
        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item9, Status.REJECTED, user2);
        when(bookingRepository.findById(1)).thenReturn(Optional.of(bookingNew));

        assertThrows(BookingException.class, () -> bookingService.updateStatusBooking(1, false, 2));
        verify(bookingRepository, never()).save(bookingNew);
        verify(bookingRepository, times(0)).save(bookingNew);
    }

    @Test
    public void testUpdateStatusBooking_isBookingEmpty() {
        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item9, Status.REJECTED, user2);
        when(bookingRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingService.updateStatusBooking(1, false, 2));
        verify(bookingRepository, never()).save(bookingNew);
        verify(bookingRepository, times(0)).save(bookingNew);
    }

    @Test
    public void testUpdateStatusBooking_isItemEmpty() {
        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item9, Status.REJECTED, user);
        when(bookingRepository.findById(1)).thenReturn(Optional.of(bookingNew));

        when(itemRepositoryJpa.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingService.updateStatusBooking(1, true, 2));
        verify(bookingRepository, never()).save(bookingNew);
        verify(bookingRepository, times(0)).save(bookingNew);
    }

    @Test
    public void testUpdateStatusBooking_isUserEmpty() {
        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item9, Status.REJECTED, user);
        when(bookingRepository.findById(1)).thenReturn(Optional.of(bookingNew));

        when(itemRepositoryJpa.findById(anyInt())).thenReturn(Optional.of(item9));
        when(userRepositoryJpa.findById(2)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingService.updateStatusBooking(1, true, 2));
        verify(bookingRepository, never()).save(bookingNew);
        verify(bookingRepository, times(0)).save(bookingNew);
    }

    @Test
    public void testCreateBooking_isSave() {
        BookingDto bookingDto = new BookingDto(10, localDateTime, localDateTime1);

        Booking bookingNew = new Booking(0, localDateTime, localDateTime1, item10, Status.WAITING, user2);

        when(userRepositoryJpa.findById(2)).thenReturn(Optional.of(user2));
        when(itemRepositoryJpa.findById(10)).thenReturn(Optional.of(item10));
        when(bookingRepository.save(bookingNew)).thenReturn(bookingNew);

        Booking booking = bookingService.create(bookingDto, 2);
        assertNotNull(booking);
        assertEquals(item10, booking.getItem());
        assertEquals(bookingNew.getItem(), booking.getItem());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    public void testCreateBooking_isNotSave() {
        BookingDto bookingDto = new BookingDto(10, localDateTime, localDateTime1);

        Booking bookingNew = new Booking(0, localDateTime, localDateTime1, item10, Status.WAITING, user2);

        when(userRepositoryJpa.findById(2)).thenReturn(Optional.of(user2));
        when(itemRepositoryJpa.findById(10)).thenReturn(Optional.of(item10));
        when(bookingRepository.save(bookingNew)).thenThrow(EntityNotFoundException.class);


        assertThrows(EntityNotFoundException.class, () -> bookingService.create(bookingDto, 2));
    }

    @Test
    public void testCreateBooking_toHimself_withThrown() {
        BookingDto bookingDto = new BookingDto(10, localDateTime, localDateTime1);
        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item10, Status.WAITING, user);

        when(userRepositoryJpa.findById(1)).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.findById(10)).thenReturn(Optional.of(item10));

        assertThrows(EntityNotFoundException.class, () -> bookingService.create(bookingDto, 1));
        verify(bookingRepository, never()).save(bookingNew);
        verify(bookingRepository, times(0)).save(bookingNew);

    }

    @Test
    public void testCreateBooking_toUserEmpty_withThrown() {
        BookingDto bookingDto = new BookingDto(10, localDateTime, localDateTime1);
        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item10, Status.WAITING, user);

        when(userRepositoryJpa.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingService.create(bookingDto, 1));
        verify(bookingRepository, never()).save(bookingNew);
        verify(bookingRepository, times(0)).save(bookingNew);
    }

    @Test
    public void testCreateBooking_toItemEmpty_withThrown() {
        BookingDto bookingDto = new BookingDto(10, localDateTime, localDateTime1);
        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item10, Status.WAITING, user);

        when(userRepositoryJpa.findById(1)).thenReturn(Optional.of(user));
        when(itemRepositoryJpa.findById(10)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingService.create(bookingDto, 1));
        verify(bookingRepository, never()).save(bookingNew);
        verify(bookingRepository, times(0)).save(bookingNew);
    }

    @Test
    public void testCreateBooking_withValidEndThrown() {
        localDateTime1 = LocalDateTime.now().minusHours(10);
        BookingDto bookingDto = new BookingDto(10, localDateTime, localDateTime1);

        Booking bookingNew = new Booking(0, localDateTime, localDateTime1, item10, Status.WAITING, user2);

        when(userRepositoryJpa.findById(2)).thenReturn(Optional.of(user2));
        when(itemRepositoryJpa.findById(10)).thenReturn(Optional.of(item10));

        assertThrows(BookingException.class, () -> bookingService.create(bookingDto, 2));
        verify(bookingRepository, never()).save(bookingNew);
        verify(bookingRepository, times(0)).save(bookingNew);
    }

    @Test
    public void testCreateBooking_withAvailableThrown() {
        BookingDto bookingDto = new BookingDto(9, localDateTime, localDateTime1);

        Booking bookingNew = new Booking(0, localDateTime, localDateTime1, item10, Status.WAITING, user2);

        when(userRepositoryJpa.findById(2)).thenReturn(Optional.of(user2));
        when(itemRepositoryJpa.findById(9)).thenReturn(Optional.of(item9));

        assertThrows(BookingException.class, () -> bookingService.create(bookingDto, 2));
        verify(bookingRepository, never()).save(bookingNew);
        verify(bookingRepository, times(0)).save(bookingNew);
    }

    @Test
    public void testCreateBooking_withTimeThrown() {
        BookingDto bookingDto = new BookingDto(10, localDateTime, localDateTime);

        Booking bookingNew = new Booking(0, localDateTime, localDateTime, item10, Status.WAITING, user2);

        when(userRepositoryJpa.findById(2)).thenReturn(Optional.of(user2));
        when(itemRepositoryJpa.findById(10)).thenReturn(Optional.of(item10));

        assertThrows(BookingException.class, () -> bookingService.create(bookingDto, 2));
        verify(bookingRepository, never()).save(bookingNew);
        verify(bookingRepository, times(0)).save(bookingNew);
    }

    @Test
    public void testGetBookingForState_isGetWaitingNotPage() {
        User user2 = new User(2, "testTestov@email.com", "nosa");
        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item10, WAITING, user2);
        //второй запрашивает
        when(userRepositoryJpa.findById(2)).thenReturn(Optional.of(user2));
        when(bookingRepository.findByBookerIdAndStatus(anyInt(), any(Status.class), any(Pageable.class))).thenReturn(List.of(bookingNew));

        Collection<Booking> bookings = bookingService.getBookingForState(2, "WAITING", 0, 1);
        assertNotNull(bookings);
        assertEquals(bookings, bookings);
    }

    @Test
    public void testGetBookingForState_isNotState() {
        assertThrows(ValidationException.class, () -> bookingService.getBookingForState(2, "WAITIIING", null, null));
    }

    @Test
    public void testGetBookingForState_isThrow() {
        when(userRepositoryJpa.findById(2)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> bookingService.getBookingForState(2, "WAITING", null, null));
    }

    @Test
    public void testGetBookingForState_isGetWaitingIsPage() {
        User user = new User(1, "test@email.com", "lasa");
        Item item = new Item(10, "table", user, "big table", true, null, new HashSet<>());
        User user2 = new User(2, "testTestov@email.com", "nosa");
        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item, WAITING, user);
        //второй запрашивает
        when(userRepositoryJpa.findById(2)).thenReturn(Optional.of(user2));
        when(bookingRepository.findByBookerIdAndStatus(anyInt(), any(Status.class), any(Pageable.class))).thenReturn(List.of(bookingNew));

        Collection<Booking> bookings = bookingService.getBookingForState(2, "WAITING", 1, 1);
        assertNotNull(bookings);
        assertEquals(bookings, bookings);
    }

    @Test
    public void testGetBookingForState_isThrowPage() {
        when(userRepositoryJpa.findById(2)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> bookingService.getBookingForState(2, "WAITING", 1, 1));
    }

    @Test
    public void testGetBookingForState_isRejectedThrowPage() {
        when(userRepositoryJpa.findById(2)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> bookingService.getBookingForState(2, "REJECTED", 1, 1));
    }

    @Test
    public void testGetBookingForState_isThrowValidPage() {
        User user2 = new User(2, "testTestov@email.com", "nosa");
        //второй запрашивает

        when(userRepositoryJpa.findById(2)).thenReturn(Optional.of(user2));

        assertThrows(IllegalArgumentException.class, () -> bookingService.getBookingForState(2, "WAITING", -2, 1));
        verify(bookingRepository, never()).findByBookerIdAndStatus(anyInt(), any(Status.class), any(Pageable.class));
        verify(bookingRepository, times(0)).findByBookerIdAndStatus(anyInt(), any(Status.class), any(Pageable.class));
    }


    @Test
    public void testGetBookingForState_isThrowWaitingNotPage() {
        User user = new User(1, "test@email.com", "lasa");

        assertThrows(ValidationException.class, () -> bookingService.getBookingForState(2, "Test", null, null));
    }

    @Test
    public void testGetBookingForOwnerAndState_isRejectedNotPage() {
        LocalDateTime localDateTime = LocalDateTime.now().plusHours(1);
        LocalDateTime localDateTime1 = LocalDateTime.now().plusHours(10);

        User user = new User(1, "test@email.com", "lasa");
        User user2 = new User(2, "testTestov@email.com", "nosa");
        Item item = new Item(10, "table", user2, "big table", true, null, new HashSet<>());

        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item, REJECTED, user);
        //второй запрашивает
        List<Booking> returnList = Arrays.asList(bookingNew);
        when(userRepositoryJpa.findById(2)).thenReturn(Optional.of(user2));
        when(itemRepositoryJpa.findByOwnerId(2)).thenReturn(Arrays.asList(new Item()));
        when(bookingRepository.findAllByItemOwnerIdAndStatus(anyInt(), any(Status.class), any(Pageable.class))).thenReturn(List.of(bookingNew));

        Collection<Booking> bookings = bookingService.getBookingForOwnerAndState(2, "REJECTED", 1, 1);
        assertNotNull(bookings);
        assertEquals(returnList, bookings);
    }

    @Test
    public void testGetBookingForOwnerAndState_isFutureNotPage() {
        LocalDateTime localDateTime = LocalDateTime.now().plusHours(1);
        LocalDateTime localDateTime1 = LocalDateTime.now().plusHours(10);

        User user = new User(1, "test@email.com", "lasa");
        User user2 = new User(2, "testTestov@email.com", "nosa");
        Item item = new Item(10, "table", user2, "big table", true, null, new HashSet<>());

        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item, WAITING, user);
        //второй запрашивает
        when(userRepositoryJpa.findById(2)).thenReturn(Optional.of(user2));
        when(itemRepositoryJpa.findByOwnerId(2)).thenReturn(Arrays.asList(new Item()));

        Collection<Booking> bookings = bookingService.getBookingForOwnerAndState(2, "FUTURE", 0, 1);
        assertNotNull(bookings);
        assertEquals(bookings, bookings);
    }

    @Test
    public void testGetBookingForOwnerAndState_isDefaultNotValidPage() {
        assertThrows(ValidationException.class, () -> bookingService.getBookingForOwnerAndState(2, "nothing", 1, 10));
    }

    @Test
    public void testGetBookingForOwnerAndState_isFutureWithPage() {
        LocalDateTime localDateTime = LocalDateTime.now().plusHours(1);
        LocalDateTime localDateTime1 = LocalDateTime.now().plusHours(10);

        User user = new User(1, "test@email.com", "lasa");
        User user2 = new User(2, "testTestov@email.com", "nosa");
        Item item = new Item(10, "table", user2, "big table", true, null, new HashSet<>());

        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item, WAITING, user);
        //второй запрашивает
        when(userRepositoryJpa.findById(2)).thenReturn(Optional.of(user2));
        when(itemRepositoryJpa.findByOwnerId(2)).thenReturn(Arrays.asList(new Item()));
        when(bookingRepository.findByItemOwnerFuture(anyInt(), any(LocalDateTime.class), any(Pageable.class))).thenReturn(List.of(bookingNew));

        Collection<Booking> bookings = bookingService.getBookingForOwnerAndState(2, "FUTURE", 0, 1);
        assertNotNull(bookings);
        assertEquals(bookings, bookings);
    }

    @Test
    public void testGetBookingForOwnerAndStateWithPage_isFutureWithPage() {
        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item10, WAITING, user);
        //второй запрашивает
        when(bookingRepository.findByItemOwnerFuture(anyInt(), any(LocalDateTime.class), any(Pageable.class))).thenReturn(List.of(bookingNew));

        Collection<Booking> bookings = bookingService.getBookingForOwnerAndStateWithPage(2, State.FUTURE, 0, 1);
        assertNotNull(bookings);
        assertEquals(bookings, bookings);
    }

    @Test
    public void testGetBookingForOwnerAndStateWithPage_isALLWithPage() {
        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item10, WAITING, user);

        when(bookingRepository.findByItemOwnerOrderByStartDesc(anyInt(), any(Pageable.class))).thenReturn(List.of(bookingNew));

        Collection<Booking> bookings = bookingService.getBookingForOwnerAndStateWithPage(2, State.ALL, 0, 1);
        assertNotNull(bookings);
        assertEquals(bookings, bookings);
    }

    @Test
    public void testGetBookingForOwnerAndStateWithPage_isCURRENTWithPage() {
        Booking bookingNew = new Booking(1, localDateTime.minusHours(1), localDateTime1, item10, WAITING, user);
        //второй запрашивает
        when(bookingRepository.findByItemOwnerCurrent(anyInt(), any(LocalDateTime.class), any(Pageable.class))).thenReturn(List.of(bookingNew));

        Collection<Booking> bookings = bookingService.getBookingForOwnerAndStateWithPage(2, State.CURRENT, 0, 1);
        assertNotNull(bookings);
        assertEquals(bookings, bookings);
    }

    @Test
    public void testGetBookingForOwnerAndStateWithPage_isPasteWithPage() {
        Booking bookingNew = new Booking(1, localDateTime.minusHours(1), localDateTime1, item10, WAITING, user);
        //второй запрашивает
        when(bookingRepository.findByItemOwnerPaste(anyInt(), any(LocalDateTime.class), any(Pageable.class))).thenReturn(List.of(bookingNew));

        Collection<Booking> bookings = bookingService.getBookingForOwnerAndStateWithPage(2, State.PAST, 0, 1);
        assertNotNull(bookings);
        assertEquals(bookings, bookings);
    }

    @Test
    public void testGetBookingForStateWithPage_isPastWithPage() {
        Booking bookingNew = new Booking(1, localDateTime.minusHours(1), localDateTime1, item10, WAITING, user);
        //второй запрашивает
        when(bookingRepository.findByBookerIdAndEndBefore(anyInt(), any(LocalDateTime.class), any(Pageable.class))).thenReturn(List.of(bookingNew));

        Collection<Booking> bookings = bookingService.getBookingForStateWithPage(2, State.PAST, 0, 1);
        assertNotNull(bookings);
        assertEquals(bookings, bookings);
    }

    @Test
    public void testGetBookingForStateWithPage_isAllWithPage() {
        Booking bookingNew = new Booking(1, localDateTime.minusHours(1), localDateTime1, item10, WAITING, user);
        //второй запрашивает
        when(bookingRepository.findAllByBookerId(anyInt(), any(Pageable.class))).thenReturn(List.of(bookingNew));

        Collection<Booking> bookings = bookingService.getBookingForStateWithPage(2, State.ALL, 0, 1);
        assertNotNull(bookings);
        assertEquals(bookings, bookings);
    }

    @Test
    public void testGetBookingForStateWithPage_isWaitingWithPage() {
        Booking bookingNew = new Booking(1, localDateTime.minusHours(1), localDateTime1, item10, WAITING, user);
        //второй запрашивает
        when(bookingRepository.findByBookerIdAndStatus(anyInt(), any(Status.class), any(Pageable.class))).thenReturn(List.of(bookingNew));

        Collection<Booking> bookings = bookingService.getBookingForStateWithPage(2, State.WAITING, 0, 1);
        assertNotNull(bookings);
        assertEquals(bookings, bookings);
    }

    @Test
    public void testGetBookingForStateWithPage_isRejectedWithPage() {
        Booking bookingNew = new Booking(1, localDateTime.minusHours(1), localDateTime1, item10, WAITING, user);
        //второй запрашивает
        when(bookingRepository.findByBookerIdAndStatus(anyInt(), any(Status.class), any(Pageable.class))).thenReturn(List.of(bookingNew));

        Collection<Booking> bookings = bookingService.getBookingForStateWithPage(2, State.REJECTED, 0, 1);
        assertNotNull(bookings);
        assertEquals(bookings, bookings);
    }

    @Test
    public void testGetBookingForStateWithPage_isCurrentWithPage() {
        Booking bookingNew = new Booking(1, localDateTime.minusHours(1), localDateTime1, item10, WAITING, user);
        //второй запрашивает
        when(bookingRepository.findByBookerIdCurrent(anyInt(), any(LocalDateTime.class), any(Pageable.class))).thenReturn(List.of(bookingNew));

        Collection<Booking> bookings = bookingService.getBookingForStateWithPage(2, State.CURRENT, 0, 1);
        assertNotNull(bookings);
        assertEquals(bookings, bookings);
    }

    @Test
    public void testGetBookingForStateWithPage_isFutureWithPage() {
        Booking bookingNew = new Booking(1, localDateTime.minusHours(1), localDateTime1, item10, WAITING, user);
        //второй запрашивает
        when(bookingRepository.findByBookerIdFuture(anyInt(), any(LocalDateTime.class), any(Pageable.class))).thenReturn(List.of(bookingNew));

        Collection<Booking> bookings = bookingService.getBookingForStateWithPage(2, State.FUTURE, 0, 1);
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

        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item, WAITING, user);
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

        Booking bookingNew = new Booking(1, localDateTime, localDateTime1, item, WAITING, user);
        //второй запрашивает
        when(userRepositoryJpa.findById(2)).thenReturn(Optional.of(user2));
        when(itemRepositoryJpa.findByOwnerId(2)).thenReturn(Arrays.asList(new Item()));

        Collection<Booking> bookings = bookingService.getBookingForOwnerAndState(2, "PAST", 1, 1);
        assertNotNull(bookings);
        assertEquals(bookings, bookings);
    }

    @Test
    public void testCheckState() {
        String str = "ALL";
        State state = bookingService.checkState(str);
        assertEquals(state.toString(), str);
    }

    @Test
    public void testCheckStateCURRENT() {
        String str = "CURRENT";
        State state = bookingService.checkState(str);
        assertEquals(state.toString(), str);
    }
}